package project.domain.directory.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.TrashPhotoDto;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.directory.mapper.GetTrashBinMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.directory.repository.TrashRepository;
import project.domain.party.entity.Party;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class TrashBinServiceImpl implements TrashBinService {

    private final TrashBinRepository trashBinRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final TrashRepository trashRepository;
    private final PhotoRepository photoRepository;
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
    public void clearTrashBin(Long trashBinId) {
        TrashBin trashBin = findById(trashBinId);
        // 휴지통에 있는 모든 dir 삭제 + photo num - 1
        List<String> dirIdList = trashBin.getDirIdList();
        // BFS인데;;
        // 휴지통에 있는 모든 file 삭제
        List<String> trasIdList = trashBin.getFileIdList();
        List<Trash> trashList = trashRepository.findAllById(trasIdList);
        ModelMapper modelMapper = new ModelMapper();
        for (Trash trash : trashList) {
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
            TrashPhotoDto trashPhotoDto = modelMapper.map(trashFileDto.getPhoto(), TrashPhotoDto.class);
            // 여기서 trashFileDto.getPhoto() => Photo로 바꿔주는 modelMapper를 쓰기위한 TrashPhotoDto를 생성해줘
            Long photoId = trashPhotoDto.getId();
            Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));
            // photoNum 감소 및 사진 삭제 로직
            int photoNum = photo.getPhotoNum() - 1;
            if (photoNum == 0) {
                photoRepository.delete(photo);
            } else {
                photo.setPhotoNum(photoNum);
                photoRepository.save(photo); // 변경된 photoNum을 저장
            }
            // 휴지통에서 제거
            trashBin.getFileIdList().remove(trash.getId());
            // 쓰레기에서 제거
            trashRepository.delete(trash);
        }
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
