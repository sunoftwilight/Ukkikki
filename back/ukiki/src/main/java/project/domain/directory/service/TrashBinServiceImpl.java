package project.domain.directory.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.DataType;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.PhotoDto;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.response.GetTrashDto;
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

    private static ModelMapper modelMapper = new ModelMapper();
    private final TrashBinRepository trashBinRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final TrashRepository trashRepository;
    private final PhotoRepository photoRepository;

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
    public List<GetTrashDto> getTrashBin(Long trashBinId) {
        log.info("getTrashBin service");
        TrashBin trashBin = findById(trashBinId);
        log.info("getTrashBin service");
        // 그냥 Trash 애들 담아서 주면된다.
        List<GetTrashDto> response = new ArrayList<>();
        // 디렉토리 자료형 담기
        // null이 아닐 경우
        List<String> dirTypeTrashIdList = trashBin.getDirTrashIdList();
        if(!dirTypeTrashIdList.isEmpty()) {
            for (String trashId : dirTypeTrashIdList) {
                Trash trash = trashRepository.findById(trashId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));
                Directory directory = modelMapper.map(trash.getContent(), Directory.class);
                GetTrashDto getTrashDto = GetTrashDto.builder()
                    .type(DataType.DIRECTORY)
                    .pk(trash.getId())
                    .name(directory.getDirName())
                    .url("none")
                    .deadLine(trash.getDeadLine())
                    .build();
                response.add(getTrashDto);
            }
        }
        // 파일 자료형 담기
        // null이 아닐 경우
        List<String> fileTypeTrashIdList = trashBin.getFileTrashIdList();
        if(!fileTypeTrashIdList.isEmpty()) {
            for (String trashId : fileTypeTrashIdList) {
                Trash trash = trashRepository.findById(trashId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));
                TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
                GetTrashDto getTrashDto = GetTrashDto.builder()
                    .type(DataType.FILE)
                    .pk(trash.getId())
                    .name("none")
                    .url(trashFileDto.getPhotoDto().getThumbUrl1())
                    .deadLine(trash.getDeadLine())
                    .build();
                response.add(getTrashDto);
            }
        }
        return response;
    }

    @Override
    public void clearTrashBin(Long trashBinId) {
        TrashBin trashBin = findById(trashBinId);
        // 휴지통에 있는 모든 dir 삭제 + photo num - 1
        List<String> dirIdList = trashBin.getDirTrashIdList();
        // BFS인데;
        // 휴지통에 있는 모든 file 삭제
        List<String> trasIdList = trashBin.getFileTrashIdList();
        List<Trash> trashList = trashRepository.findAllById(trasIdList);
        ModelMapper modelMapper = new ModelMapper();
        for (Trash trash : trashList) {
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
            PhotoDto trashPhotoDto = trashFileDto.getPhotoDto();
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
            trashBin.getFileTrashIdList().remove(trash.getId());
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
    public void saveFileToTrashBin(Trash fileTrash) {
        // file -> photo -> partyId -> trashBin -> addfileId
        TrashFileDto trashFileDto = modelMapper.map(fileTrash.getContent(), TrashFileDto.class);
        File file = fileRepository.findById(trashFileDto.getId())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
        log.info("saveFileToTrashBin");
        log.info("{}", file);
        Long partyId = file.getPhotoDto().getPartyId();
        log.info("saveFileToTrashBin");
        TrashBin trashBin = findById(partyId);
        trashBin.getFileTrashIdList().add(fileTrash.getId());
        trashBinRepository.save(trashBin);
    }


    @Override
    public List<String> getDirNameList(TrashBin trashBin) {
        List<String> dirNameList = new ArrayList<>();
        List<Directory> dirList = directoryRepository.findAllById(trashBin.getDirTrashIdList());
        for (Directory dir : dirList) {
            dirNameList.add(dir.getDirName());
        }
        return dirNameList;
    }

    @Override
    public List<String> getPhotoUrlList(TrashBin trashBin) {
        List<String> photoUrlList = new ArrayList<>();
        List<File> FileList = fileRepository.findAllById(trashBin.getFileTrashIdList());
        for (File file : FileList) {
            photoUrlList.add(file.getPhotoDto().getPhotoUrl());
        }
        return photoUrlList;
    }



}
