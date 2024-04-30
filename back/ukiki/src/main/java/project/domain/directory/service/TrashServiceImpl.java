package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.Directory;
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
    public Trash save(Directory directory) {
        return trashRepository.save(Trash.builder()
            .directoryId(directory.getId())
            .dirName(directory.getDirName())
            .deadLine(LocalDateTime.now().plusWeeks(2))
            .parentDirId(directory.getParentDirId())
            .childDirIdList(directory.getChildDirIdList())
            .photoList(directory.getPhotoList())
            .build());
    }

    @Transactional
    public Integer realDelete() {
        LocalDateTime now = LocalDateTime.now();
        List<Trash> trashes = trashRepository.deleteTrashesByDeadLineIsBefore(now);
        for (Trash dir : trashes) {
            List<Long> photoList = dir.getPhotoList();
            for (Long photoId : photoList) {
                Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));
                int photoNum = photo.getPhotoNum();
                photo.setPhotoNum(photoNum - 1);
                if (photoNum - 1 == 0) {
                    //S3 삭제 요청

                    photoRepository.delete(photo);
                }
            }
        }
        return trashes.size();
    }

    @Override
    public Trash findById(String directoryId) {
        return trashRepository.findById(directoryId).orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
    }

    @Override
    public Boolean isOutOfRecoveryPeriod(Trash deletedDir) {
        LocalDate deadLine = deletedDir.getDeadLine().toLocalDate();
        LocalDate now = LocalDate.now();
        return deadLine.isAfter(now);
    }

}
