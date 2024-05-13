package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.request.TrashIdListDto;

public interface TrashService {

    void getTrash();

    void restoreOneTrash(String trashId, Long trashBinId, String sseKey);

    void restoreTrashList(List<String> trashIdList, Long trashBinId, String sseKey);

    void deleteOneTrash(String trashId, Long trashBinId);

    void deleteTrashList(Long trashBinId, List<String> trashIdList);

    Trash findById(String directoryId);

    Boolean isOutOfRecoveryPeriod(Trash deletedData);

    String generateId();

    // 휴지통에서 제거
    void deleteDirFromTrashBin(String dirId, Long trashBinId);

    // 휴지통에서 제거
    void deleteFileFromTrashBin(String fileId, Long trashBinId);

    // 해당 폴더를 포함한 모든 자식폴더, 사진 폴더를 반환함
    List<Trash> getAllTrash(String TrashIdDirType);

    List<Trash> getAllTrashV2(String trashIdDirType);

    // 파일 생성
    void setFile(Trash trash, TrashFileDto trashFileDto, String sseKey);

    // 디렉토리 생성
    void setDir(Trash trash, Directory trashDirDto);

    void setConnection(Trash trash);

    void setEndPoint(Trash trash, List<String> fullRoutList, int sizeOfFullRoutList);
}
