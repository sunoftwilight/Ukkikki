package project.domain.directory.service;

import project.domain.directory.collection.Trash;
import project.domain.directory.dto.response.GetTrashBinDto;

public interface TrashService {

    void getTrash();

    GetTrashBinDto restoreTrash(String trashId, Long trashBinId);

    public Integer realDelete();

    public Trash findById(String directoryId);

    public Boolean isOutOfRecoveryPeriod(Trash deletedData);

    public String generateId();

    public void restoreDir(String dirId, Long trashBinId);

    public void restoreFile(String fileId, Long trashBinId);

}
