package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.directory.dto.response.GetTrashDto;
import project.domain.party.entity.Party;

public interface TrashBinService {

    void createTrashBin(Party party);

    void createTrashBinTest(Long partyId);

    List<GetTrashDto> getTrashBin(Long trashBinId);

    void clearTrashBin(Long trashBinId);

    List<String> getDirNameList(TrashBin trashBin);

    List<String> getPhotoUrlList(TrashBin trashBin);

    TrashBin findById(Long trashBinId);

    void saveFileToTrashBin(Trash fileTrash);

}
