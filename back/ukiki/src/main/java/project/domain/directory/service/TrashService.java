package project.domain.directory.service;

import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.response.GetTrashBinDto;

public interface TrashService {


    public Trash saveDir(Directory directory);

    Trash saveFile(File file, String dirId);

    void getTrash();

    GetTrashBinDto restoreTrash(String trashId, Long trashBinId);

    public Integer realDelete();

    public Trash findById(String directoryId);

    public Boolean isOutOfRecoveryPeriod(Trash deletedData);

    public String generateId();

}
