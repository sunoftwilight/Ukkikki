package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.DataType;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.TrashFileDto;
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
    private final DirectoryRepository directoryRepository;
    private final TrashBinRepository trashBinRepository;
    private final TrashRepository trashRepository;
    private final FileRepository fileRepository;

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
        // 유효성 검사
        if (isOutOfRecoveryPeriod(trash)) {
            throw new BusinessLogicException(ErrorCode.DIRECTORY_OUT_OF_DEADLINE);
        }

        // 폴더인지 파일인지 분기 처리
        if (trash.getDataType() == DataType.DIRECTORY) {
            log.info("DataType = DIRECTORY");
            List<Trash> allTrash = getAllTrash(trash.getId());
            log.info("모든 쓰레기 = {}", allTrash);
            // 일단 가장 첫번째인 폴더는 휴지통에서 제거 + 본 디렉토리에서 관계 회복
            Directory trashDirDto = modelMapper.map(trash.getContent(), Directory.class);
            setConnection(trash);

            for (Trash oneTrash : allTrash) {
                log.info("oneTrash = {}", oneTrash);
                if (oneTrash.getDataType() == DataType.DIRECTORY) {
                    log.info("디렉토리타입 쓰레기 복원 작업");
                    Directory oneTrashDirDto = modelMapper.map(oneTrash.getContent(), Directory.class);
                    // 폴더 복원
                    setDir(oneTrash, oneTrashDirDto);
                    // 휴지통에서 제거
                    trashRepository.delete(oneTrash);

                } else if (oneTrash.getDataType() == DataType.FILE) {
                    log.info("파일타입 쓰레기 복원 작업 시작");
                    // content 가져오기
                    TrashFileDto oneTrashFileDto = modelMapper.map(oneTrash.getContent(),
                        TrashFileDto.class);
                    // file 복원
                    setFile(oneTrash, oneTrashFileDto);
                    // 쓰레기 지우기
                    trashRepository.delete(oneTrash);
                }
            }
            // 휴지통에서 제거
            trashBin.getDirTrashIdList().remove(trash.getId());
            trashBinRepository.save(trashBin);
            return;

        } else if (trash.getDataType() == DataType.FILE) {
            log.info("DataType = FILE");
            // content 가져오기
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
            // dir 관계 복구
            setConnection(trash);
            // file 복구
            setFile(trash, trashFileDto);
            // trashBin 제거
            deleteFileFromTrashBin(trash.getId(), trashBinId);
            // trash 제거
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

    // dir 생성
    @Override
    public void setDir(Trash trash, Directory trashDirDto) {
        Optional<Directory> opDir = directoryRepository.findById(trash.getRawId());
        // 폴더에 없는 경우 쓰레기 정보를 바탕으로 새로 만들어 저장
        if(opDir.isEmpty()) {
            directoryRepository.save(Directory.builder()
                .id(trash.getRawId())
                .dirName(trashDirDto.getDirName())
                .parentDirId(trashDirDto.getParentDirId())
                .childDirIdList(trashDirDto.getChildDirIdList())
                .fileIdList(trashDirDto.getFileIdList())
                .build());
        } else {
        // 복구하려는 폴더가 이미 공유 앨범에 생성 된 상태일때 쓰레기 정보를 추가
            Directory directory = opDir.get();
            List<String> childDirIdList = directory.getChildDirIdList();
            List<String> fileIdList = directory.getFileIdList();

            // childDirIdList 정보 추가
            for(String childDirId : trashDirDto.getChildDirIdList()) {
                if(childDirIdList.contains(childDirId)){continue;}
                childDirIdList.add(childDirId);
            }
            // fileIdList 정보 추가
            fileIdList.addAll(trashDirDto.getFileIdList());
            // 저장
            directoryRepository.save(directory);
        }
    }

    // file 생성
    @Override
    public void setFile(Trash trash, TrashFileDto trashFileDto) {
        Optional<File> opFile = fileRepository.findById(trash.getRawId());
        // 폴더에 이미 file이 있는경우 그놈한테 dirId 등록
        if (opFile.isPresent()) {
            File file = opFile.get();
            file.getDirIdList().add(trashFileDto.getDirId());
            fileRepository.save(file);
            return;
        }
        // 없다면 새로 file을 만들어 주기
        fileRepository.save(
            File.builder()
                .id(trash.getRawId())
                .photoDto(trashFileDto.getPhotoDto())
                .dirIdList(Arrays.asList(trashFileDto.getDirId()))
                .build()
        );
    }


    // trash 복구를 위한 빈 폴더 생성과 endPoint 연결 사전 작업
    public void setConnection(Trash trash) {
        List<String> fullRoutList = trash.getFullRout();
        List<String> fullNameList = trash.getFullName();
        int sizeOfFullRoutList = fullRoutList.size();
        // root에서 지워진 경우
        if(sizeOfFullRoutList == 1) {
            // 끝맺고 마무리
            setEndPoint(trash, fullRoutList, sizeOfFullRoutList);
            return;
        }

        // 깡통 폴더 생성
        for(int i = 2; i < sizeOfFullRoutList; i++) {
            Optional<Directory> opDir = directoryRepository.findById(fullRoutList.get(i));
            if(!opDir.isEmpty()) {continue;}
            //-- i에 해당하는 폴더가 없는경우 => 새로 만들어 줘야됨
            Directory parentDir = directoryRepository.findById(fullRoutList.get(i - 1))
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
            // 부모에 자식 dirId 등록
            parentDir.getChildDirIdList().add(fullRoutList.get(i));
            directoryRepository.save(parentDir);
            // 자식 dir 생성
            directoryRepository.save(
                Directory.builder()
                    .id(fullRoutList.get(i))
                    .dirName(fullNameList.get(i))
                    .parentDirId(fullRoutList.get(i - 1))
                    .build()
            );
        }

        setEndPoint(trash, fullRoutList, sizeOfFullRoutList);
    }

    public void setEndPoint(Trash trash, List<String> fullRoutList, int sizeOfFullRoutList) {
        // endPoint에 fileId or dirId 부여하기
        Directory directory = directoryRepository.findById(
                fullRoutList.get(sizeOfFullRoutList - 1))
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        switch (trash.getDataType()) {
            case FILE:
                directory.getFileIdList().add(trash.getRawId());
                break;
            case DIRECTORY:
                directory.getChildDirIdList().add(trash.getRawId());
                break;
            default:
                throw new BusinessLogicException(ErrorCode.TRASH_CLASSIFICATION_FAIL);
        }
        directoryRepository.save(directory);
    }

}
