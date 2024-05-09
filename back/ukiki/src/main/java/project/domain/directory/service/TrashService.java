package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.response.GetTrashBinDto;

public interface TrashService {

    void getTrash();

    void restoreTrash(String trashId, Long trashBinId);

    public Integer realDelete();

    public Trash findById(String directoryId);

    public Boolean isOutOfRecoveryPeriod(Trash deletedData);

    public String generateId();

    // 휴지통에서 제거
    public void deleteDirFromTrashBin(String dirId, Long trashBinId);

    // 휴지통에서 제거
    public void deleteFileFromTrashBin(String fileId, Long trashBinId);

    // 해당 폴더를 포함한 모든 자식폴더, 사진 폴더를 반환함

    /**
     * 인자로 받은 TrashI에 해당하는 Trash를 포함한 하위의 모든 Trash를 반환
     *
     * @param TrashIdDirType : 유저가 휴지통에서 찍은 그 폴더나 파일의 TrashId
     * @return : 유저가 찍은 폴더와 사진, 해당 하위 폴더와 사진 Trash를 반환
     */
    List<Trash> getAllTrash(String TrashIdDirType);

}
