package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class TrashServiceImpl implements TrashService{

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
    public GetTrashBinDto restoreTrash(String trashId, Long trashBinId) {
        TrashBin trashBin = trashBinService.findById(trashBinId);

        // 쓰레기 불러오기
        Trash trash = findById(trashId);
        if (isOutOfRecoveryPeriod(trash)) {
            throw new BusinessLogicException(ErrorCode.DIRECTORY_OUT_OF_DEADLINE);
        }
        ModelMapper modelMapper = new ModelMapper();

        // 폴더인지 파일인지 분기 처리
        if (trash.getDataType() == DataType.DIRECTORY) {
            // content 추출하기
            Directory deletedDir = modelMapper.map(trash.getContent(), Directory.class);

            // content를 바탕으로 directory 복원
            Directory restoredDir = Directory.builder()
                .id(deletedDir.getId())
                .dirName(deletedDir.getDirName())
                .parentDirId(deletedDir.getParentDirId())
                .childDirIdList(deletedDir.getChildDirIdList())
                .fileIdList(deletedDir.getFileIdList())
                .build();

            // 부모 폴더의 자식 list에 추가하기 => 부모가 directory에 있으면 진행 없으면 찾기
            Directory parentDir = directoryService.findById(deletedDir.getParentDirId());
            parentDir.getChildDirIdList().add(deletedDir.getId());

            // 변경 사항 저장
            directoryRepository.saveAll(directoryService.toList(restoredDir, parentDir));

            // trashBin에서 trash제거
            restoreDir(deletedDir.getId(), trashBinId);

            // 쓰래기에서 제거
            trashRepository.delete(trash);
            return getTrashBinMapper.toGetTrashBinDto(
                trashBin,
                trashBinService.getDirNameList(trashBin),
                trashBinService.getPhotoUrlList(trashBin)
            );
        } else if (trash.getDataType() == DataType.FILE) {
            // content 가져오기
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);

            // 연관관계에 있는 directory, file 불러오기
            // 해당 dir 마저 삭제된 상태면 복구 불가
            Directory dir = directoryService.findById(trashFileDto.getDirId());

            // 없을떄(휴지통에 있는 파일이 마지막일때) 그냥 file을 새로 만들어서 dir와 관계 설정 해줘야됨
            try {
                File file = fileService.findById(trashFileDto.getId());
            } catch (BusinessLogicException e) {

                // 새로 file을 생성해서 저장하기
                fileRepository.save(File.builder()
                    .id(trashFileDto.getId())
                    .photo(trashFileDto.getPhoto())
                    .dirIdList(Arrays.asList(trashFileDto.getDirId()))
                    .build());

                // 해당 파일이 위치한 폴더의 fileList에 file 추가하기
                Directory findDir = directoryService.findById(trashFileDto.getDirId());
                findDir.getFileIdList().add(trashFileDto.getId());
                directoryRepository.save(findDir);

                // 휴지통에서 file 제거
                restoreFile(trashFileDto.getId(), trashBinId);
                // 쓰레기에서 file 제거
                trashRepository.delete(trash);
                return getTrashBinMapper.toGetTrashBinDto(
                    trashBin,
                    trashBinService.getDirNameList(trashBin),
                    trashBinService.getPhotoUrlList(trashBin)
                );
            }
            // 해당 폴더의 fileIdList에 fileId 추가
            fileService.setDirFileRelation(trashFileDto.getDirId(), trash.getId());

            // 쓰레기 통에서 file 제거
            restoreFile(trashFileDto.getId(), trashBinId);

            // 쓰레기에서 file 제거
            trashRepository.delete(trash);

            return getTrashBinMapper.toGetTrashBinDto(
                trashBin,
                trashBinService.getDirNameList(trashBin),
                trashBinService.getPhotoUrlList(trashBin)
            );
        }
        // 저장하기
        throw new BusinessLogicException(ErrorCode.TRASH_CLASSIFICATION_FAIL);
    }

    @Transactional
    public Integer realDelete() {
        LocalDateTime now = LocalDateTime.now();
        List<Trash> trashes = trashRepository.deleteTrashesByDeadLineIsBefore(now);
        // 삭제 로직 보충 필요!!
        return trashes.size();
    }

    @Override
    public Trash findById(String directoryId) {
        return trashRepository.findById(directoryId).orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
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
    public void restoreDir(String dirId, Long trashBinId) {
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getDirIdList().remove(dirId);
        trashBinRepository.save(trashBin);
    }

    @Override
    public void restoreFile(String fileId, Long trashBinId) {
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getFileIdList().remove(fileId);
        trashBinRepository.save(trashBin);
    }

}
