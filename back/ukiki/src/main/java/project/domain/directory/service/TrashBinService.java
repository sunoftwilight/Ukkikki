package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.directory.dto.response.GetTrashDto;
import project.domain.party.entity.Party;

public interface TrashBinService {

    public void createTrashBin(Party party);

    public List<GetTrashDto> getTrashBin(Long trashBinId);

    void clearTrashBin(Long trashBinId);

    public List<String> getDirNameList(TrashBin trashBin);

    public List<String> getPhotoUrlList(TrashBin trashBin);

    public TrashBin findById(Long trashBinId);

    public void saveFileToTrashBin(Trash fileTrash);

}
