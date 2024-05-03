package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.party.entity.Party;

public interface TrashBinService {

    public void createTrashBin(Party party);

    public GetTrashBinDto getTrashBin(Long trashBinId);

    public List<String> getDirNameList(TrashBin trashBin);

    public List<String> getPhotoUrlList(TrashBin trashBin);

    public void restoreDir(String dirId, Long trashBinId);

    public void restoreFile(String fileId, Long trashBinId);

    public TrashBin findById(Long trashBinId);

    public void saveFile(String fileId);

    public void saveDir(Directory dir);
}
