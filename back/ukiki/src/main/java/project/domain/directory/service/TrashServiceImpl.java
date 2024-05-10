package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.DataType;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.directory.mapper.GetTrashBinMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.directory.repository.TrashRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class TrashServiceImpl implements TrashService{


    private static ModelMapper modelMapper = new ModelMapper();

    private TrashBinService trashBinService;
    private DirectoryService directoryService;
    private final FileService fileService;
    private final DirectoryRepository directoryRepository;
    private final TrashBinRepository trashBinRepository;
    private final TrashRepository trashRepository;
    private final FileRepository fileRepository;

    private final GetTrashBinMapper getTrashBinMapper;


    @Override
    public void getTrash() {

    }

    @Override
    @Transactional
    // trashId != rawId
    public void restoreOneTrash(String trashId, Long trashBinId) {
        TrashBin trashBin = trashBinService.findById(trashBinId);
        Trash trash = trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));

        if (isOutOfRecoveryPeriod(trash)) {
            throw new BusinessLogicException(ErrorCode.DIRECTORY_OUT_OF_DEADLINE);
        }

        // 폴더인지 파일인지 분기 처리
        if (trash.getDataType() == DataType.DIRECTORY) {
            log.info("DataType = DIRECTORY");
            List<Trash> allTrash = getAllTrash(trash.getId());
            log.info("모든 쓰레기 = {}", allTrash);
            // 일단 가장 첫번째인 폴더는 휴지통에서 제거 + 본 디렉토리에서 관계 회복
            Directory deletedRootDir = modelMapper.map(trash.getContent(), Directory.class);

            // 부모 폴더의 자식 list에 추가하기 => 부모가 directory에 있으면 진행 없으면 찾기
            Directory parentDir = directoryService.findById(deletedRootDir.getParentDirId());
            parentDir.getChildDirIdList().add(deletedRootDir.getId());
            // 변경 사항 저장
            directoryRepository.save(parentDir);

            for (Trash oneTrash : allTrash) {
                // 해당 파일 타입 분류 후 그냥 그대로 save만 해주면 된다.
                log.info("oneTrash = {}", oneTrash);
                if (oneTrash.getDataType() == DataType.DIRECTORY) {
                    log.info("디렉토리타입 쓰레기 복원 작업");
                    // content 추출하기
                    Directory deletedDir = modelMapper.map(oneTrash.getContent(), Directory.class);
                    // content를 바탕으로 directory 복원
                    Directory restoredDir = Directory.builder()
                        .id(deletedDir.getId())
                        .dirName(deletedDir.getDirName())
                        .parentDirId(deletedDir.getParentDirId())
                        .childDirIdList(deletedDir.getChildDirIdList())
                        .fileIdList(deletedDir.getFileIdList())
                        .build();
                    directoryRepository.save(restoredDir);
                    trashRepository.delete(oneTrash);

                } else if (oneTrash.getDataType() == DataType.FILE) {
                    log.info("파일타입 쓰레기 복원 작업 시작");
                    // content 가져오기
                    TrashFileDto trashFileDto = modelMapper.map(oneTrash.getContent(),
                        TrashFileDto.class);

                    // file이 있을때 => 그냥 그 file의 dirId에가다 dirId 추가
                    try {
                        File file = fileRepository.findById(trashFileDto.getId())
                            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 filedId"));
                    } catch (IllegalArgumentException e) {
                        // 새로 file을 만들기
                        fileRepository.save(File.builder()
                            .id(trashFileDto.getId())
                            .photoDto(trashFileDto.getPhotoDto())
                            .dirIdList(Arrays.asList(trashFileDto.getDirId()))
                            .build()
                        );
                        // trash제거
                        trashRepository.delete(oneTrash);
                        continue;
                    }
                    log.info("file에 있는경우");
                    // 있으면 기존의 file의 dirIdList에 추가, 여기서 복원된 dir은 해당 file의 정보를 가지고 있다.
                    // 그래서 dir의 fileIdList는 별도로 작업이 필요없다.
                    File file = fileService.findById(trashFileDto.getId());
                    file.getDirIdList().add(trashFileDto.getDirId());
                    fileRepository.save(file);
                    trashRepository.delete(oneTrash);
                }
            }
            // 휴지통에서 제거
            trashBin.getDirTrashIdList().remove(trash.getId());
            trashBinRepository.save(trashBin);
            // 쓰래기에서 제거
            return;


        } else if (trash.getDataType() == DataType.FILE) {
            log.info("DataType = FILE");
            // content 가져오기
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);

            // 연관관계에 있는 directory, file 불러오기
            // 해당 dir 마저 삭제된 상태면 복구 불가
            // setDir
            Directory dir = directoryService.findById(trashFileDto.getDirId());

            // 없을떄(휴지통에 있는 파일이 마지막일때) 그냥 file을 새로 만들어서 dir와 관계 설정 해줘야됨
            try {
                log.info("디렉토리에서 복원 사진 찾기 시도 rawId = {}", trashFileDto.getId());
                File file = fileRepository.findById(trashFileDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 fileId입니다."));
            } catch (IllegalArgumentException e) {
                log.info("디렉토이에 사진이 없어서 새로 만들기");
                // 새로 file을 생성해서 저장하기
                fileRepository.save(File.builder()
                    .id(trashFileDto.getId())
                    .photoDto(trashFileDto.getPhotoDto())
                    .dirIdList(Arrays.asList(trashFileDto.getDirId()))
                    .build());

                // 해당 파일이 위치한 폴더의 fileList에 file 추가하기
                Directory findDir = directoryService.findById(trashFileDto.getDirId());
                findDir.getFileIdList().add(trashFileDto.getId());
                directoryRepository.save(findDir);

                // 휴지통에서 file 제거
                deleteFileFromTrashBin(trash.getId(), trashBinId);
                // 쓰레기에서 file 제거
                trashRepository.deleteById(trash.getId());
                return;
            }
            log.info("너 이쪽으로 오니?");
            // 해당 폴더의 fileIdList에 fileId 추가= > 폴더
            fileService.setDirFileRelation(trashFileDto.getDirId(), trashFileDto.getId());

            // 쓰레기 통에서 file 제거 rawId임=> 쓰레기 통
            deleteFileFromTrashBin(trash.getId(), trashBinId);
            // 쓰레기에서 trash제거
            trashRepository.delete(trash);
            return;
        }
        throw new BusinessLogicException(ErrorCode.TRASH_CLASSIFICATION_FAIL);
    }

    @Override
    public void restoreTrashList(List<String> trashIdList, Long trashBinId) {

    }

    @Override
    public List<Trash> getAllTrash(String trashIdDirType) {
        List<Trash> result = new ArrayList<>();

        // 초기화
        Deque<Trash> deque = new ArrayDeque<>();
        HashSet<String> visitedSet = new HashSet<>();
        Trash trash = findById(trashIdDirType);
        deque.addFirst(trash);
        visitedSet.add(trashIdDirType);
        result.add(trash);

        LocalDate deadLine = trash.getDeadLine();
        while (!deque.isEmpty()) {
            // 디큐, result에 저장, 모든 file의 TrashId 저장
            Trash curTrash = deque.pollLast();
            // trashDirType 저장
            Directory curDirInTrash = modelMapper.map(curTrash.getContent(), Directory.class);
            List<String> fileRawIdList = curDirInTrash.getFileIdList();

            if (!fileRawIdList.isEmpty()) {
                for (String fileRawId : fileRawIdList) {
                    List<Trash> trashFileTypeList = trashRepository.findByRawId(fileRawId);
                    log.info("List<Trash> = {}", trashFileTypeList);
                    if(trashFileTypeList.isEmpty()) {
                        throw new BusinessLogicException(ErrorCode.FILE_NOT_FOUND);
                    }
                    Trash trashFileType = null;
                    for (Trash fileTrash : trashFileTypeList) {
                        TrashFileDto trashFileDto = modelMapper.map(fileTrash.getContent(),
                            TrashFileDto.class);
                        if (trashFileDto.getDirId().equals(curDirInTrash.getId())) {
                            trashFileType = fileTrash;
                            break;
                        }
                    }
                    if (trashFileType == null) {
                        throw new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND);
                    }
                    log.info("검색된 TrashFileType = {}", trashFileType);
                    result.add(trashFileType);
                }
            }
            // 탐색
            // trashToDir 저장
            List<String> childDirRawIdList = curDirInTrash.getChildDirIdList();
            if (!childDirRawIdList.isEmpty()) {
                for (String dirRawId : childDirRawIdList) {
                    if(visitedSet.contains(dirRawId)){
                        continue;
                    }
                    Trash trashDirType = trashRepository.findFirstByRawId(dirRawId)
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
                    deque.addFirst(trashDirType);
                    visitedSet.add(dirRawId);
                    result.add(trashDirType);
                }
            }
        }
        // 다음 요청을 위해 방문체크 지워주기
        visitedSet.clear();
        return result;
    }

    @Transactional
    public Integer realDelete() {
        LocalDateTime now = LocalDateTime.now();
        List<Trash> trashes = trashRepository.deleteTrashesByDeadLineIsBefore(now);
        // 삭제 로직 보충 필요!!
        return trashes.size();
    }

    @Override
    public Trash findById(String trashId) {
        return trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
    }

    @Override
    public Boolean isOutOfRecoveryPeriod(Trash deletedDate) {
        LocalDate deadLine = deletedDate.getDeadLine();
        LocalDate now = LocalDate.now();
        return deadLine.isBefore(now);
    }

    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID()).append(LocalDateTime.now());
        return String.valueOf(sb);
    }

    @Override
    public void deleteDirFromTrashBin(String trashId, Long trashBinId) {
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getDirTrashIdList().remove(trashId);
        trashBinRepository.save(trashBin);
    }

    @Override
    public void deleteFileFromTrashBin(String trashId, Long trashBinId) {
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getFileTrashIdList().remove(trashId);
        trashBinRepository.save(trashBin);
    }

}
