package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.directory.repository.TrashRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
public class TrashServiceImpl implements TrashService{

    private DirectoryService directoryService;
    private final FileService fileService;
    private final DirectoryRepository directoryRepository;
    private final TrashBinRepository trashBinRepository;
    private final TrashRepository trashRepository;


    @Override
    public void getTrash() {

    }

    @Override
    @Transactional
    public GetTrashBinDto restoreTrash(String trashId, Long trashBinId) {
        // 쓰레기 통에서 가져오기
        Trash trash = findById(trashId);
        // 오늘이 deadLine보다 크다면 복구 불가능
        if (isOutOfRecoveryPeriod(trash)) {
            throw new BusinessLogicException(ErrorCode.DIRECTORY_OUT_OF_DEADLINE);
        }
        ModelMapper modelMapper = new ModelMapper();
        // 새롭게 태어나기
        if (trash.getDataType() == DataType.DIRECTORY) {
            // json to class
            Directory deletedDir = modelMapper.map(trash.getContent(), Directory.class);
            Directory restoredDir = Directory.builder()
                .id(deletedDir.getId())
                .dirName(deletedDir.getDirName())
                .parentDirId(deletedDir.getParentDirId())
                .childDirIdList(deletedDir.getChildDirIdList())
                .fileIdList(deletedDir.getFileIdList())
                .build();
            // 부모에 이어붙여주기 => 부모가 directory에 있으면 진행 없으면 찾기
            Directory parentDir = directoryService.findById(deletedDir.getParentDirId());
            parentDir.getChildDirIdList().add(deletedDir.getId());
            directoryRepository.saveAll(directoryService.toList(restoredDir, parentDir));
            // 기존 휴지통에 있었던 그놈 삭제하기
            restoreDir(deletedDir.getId(), trashBinId);
            trashRepository.delete(trash);
            return new GetTrashBinDto();
        } else if (trash.getDataType() == DataType.FILE) {
            // content 가져오기
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
            // directory, file 가져오기
            Directory dir = directoryService.findById(trashFileDto.getDirId());

            // 없을떄 그냥 file 추가해주는 설정으로 해줘야됨
            try {
                File file = fileService.findById(trashFileDto.getId());
            } catch (BusinessLogicException b) {
                // 새로 file을 생성해서 저장하기

                return new GetTrashBinDto();
            }
            // 관계 설정
            fileService.setDirFileRelation(trashFileDto.getDirId(), trash.getId());
            restoreFile(trashFileDto.getId(), trashBinId);
            trashRepository.delete(trash);
            return new GetTrashBinDto();
        }
        // 저장하기
        return null;

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
