package project.domain.directory.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.directory.mapper.GetTrashBinMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.party.entity.Party;
import project.domain.photo.entity.Photo;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class TrashBinServiceImpl implements TrashBinService {

    private final TrashBinRepository trashBinRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final GetTrashBinMapper getTrashBinMapper;

    @Override
    public void createTrashBin(Party party) {
        // TrashBin 생성
        trashBinRepository.save(
            TrashBin.builder()
                .id(party.getId())
                .trashBinName(party.getPartyName().concat(" 휴지통"))
                .build());
    }

    @Override
    public GetTrashBinDto getTrashBin(Long trashBinId) {
        TrashBin trashBin = findById(trashBinId);
        List<String> dirNameList = getDirNameList(trashBin);
        List<String> photoUrlList = getPhotoUrlList(trashBin);
        return getTrashBinMapper.toGetTrashBinDto(trashBin, dirNameList, photoUrlList);
    }

    @Override
    public TrashBin findById(Long trashBinId) {
        return trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));

    }

    @Override
    public void saveFile(String fileId) {
        // file -> photo -> partyId -> trashBin -> addfileId
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
        ModelMapper modelMapper = new ModelMapper();
        Photo photo = modelMapper.map(file.getPhoto(), Photo.class);
        // partyId == trashBinId
        Long partyId = photo.getParty().getId();
        TrashBin trashBin = findById(partyId);
        trashBin.getFileIdList().add(fileId);
        trashBinRepository.save(trashBin);
    }


    @Override
    public List<String> getDirNameList(TrashBin trashBin) {
        List<String> dirNameList = new ArrayList<>();
        List<Directory> dirList = directoryRepository.findAllById(trashBin.getDirIdList());
        for (Directory dir : dirList) {
            dirNameList.add(dir.getDirName());
        }
        return dirNameList;
    }

    @Override
    public List<String> getPhotoUrlList(TrashBin trashBin) {
        List<String> photoUrlList = new ArrayList<>();
        List<File> FileList = fileRepository.findAllById(trashBin.getFileIdList());
        ModelMapper modelMapper = new ModelMapper();
        for (File file : FileList) {
            Photo photo = modelMapper.map(file.getPhoto(), Photo.class);
            photoUrlList.add(photo.getPhotoUrl().getPhotoUrl());
        }
        return photoUrlList;
    }




}
