package project.domain.directory.service;

import java.time.LocalDateTime;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;

public interface TrashService {

    public Trash saveDir(Directory directory);

    public Trash saveFile(File file);

    public Integer realDelete();

    public Trash findById(String directoryId);

    public Boolean isOutOfRecoveryPeriod(Trash deletedData);

    public String generateId();

    // 디렉토리 및 사진 복원

}
