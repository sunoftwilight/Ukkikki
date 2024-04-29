package project.domain.directory.service;

import java.time.LocalDateTime;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.Trash;

public interface TrashService {

    public Trash save(Directory directory);

    public Boolean realDelete();

    public Trash findById(String directoryId);
}
