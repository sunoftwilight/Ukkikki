package project.domain.directory.service;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.Trash;
import project.domain.directory.repository.TrashRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
public class TrashServiceImpl implements TrashService{

    private final TrashRepository trashRepository;

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

    @Override
    public Boolean realDelete() {
        return null;
    }

    @Override
    public Trash findById(String directoryId) {
        return trashRepository.findById(directoryId).orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
    }

}
