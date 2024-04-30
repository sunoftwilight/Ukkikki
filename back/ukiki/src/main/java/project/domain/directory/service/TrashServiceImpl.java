package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.DataType;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.repository.TrashRepository;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
public class TrashServiceImpl implements TrashService{

    private final TrashRepository trashRepository;
    private final PhotoRepository photoRepository;

    @Override
    public Trash saveDir(Directory dir) {
        return trashRepository.save(Trash.builder()
            .id(dir.getId())
            .dataType(DataType.DIRECTORY)
            .content(dir)
            .deadLine(LocalDate.now().plusWeeks(2))
            .build());
    }

    @Override
    public Trash saveFile(File file) {
        return trashRepository.save(Trash.builder()
            .id(file.getFileId())
            .dataType(DataType.FILE)
            .content(file)
            .deadLine(LocalDate.now().plusWeeks(2))
            .build());
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

}
