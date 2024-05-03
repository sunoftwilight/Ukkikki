package project.domain.directory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.mapper.GetDirMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService{

    private final DirectoryService directoryService;
    private final TrashService trashService;
    private final TrashBinService trashBinService;
    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final PhotoRepository photoRepository;
    private final GetDirMapper getDirMapper;


    @Override
    @Transactional
    public void createFile(Long partyId, Photo photo) {
        // 파티에서 rootDirId 찾기
        Party findParty = partyRepository.findById(partyId).orElseThrow(() -> new BusinessLogicException(
            ErrorCode.PARTY_NOT_FOUND));
        // file객체 생성하기
        File newFile = File.builder()
            .fileId(generateId())
            .photo(photo)
            .build();

        String rootDirId = findParty.getRootDirId();
        String newFileId = newFile.getFileId();
        setDirFileRelation(rootDirId, newFileId);
    }

    @Override
    @Transactional
    public GetDirDto copyFile(String fileId, String fromDirId, String toDirId) {
        setDirFileRelation(toDirId, fileId);
        // photo num ++1
        File findFile = findById(fileId);
        ModelMapper modelMapper = new ModelMapper();
        Photo photo = modelMapper.map(findFile.getPhoto(), Photo.class);
        Long photoId = photo.getId();
        Photo findPhoto = photoRepository.findById(photoId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));
        int photoNum = findPhoto.getPhotoNum();
        findPhoto.setPhotoNum(photoNum + 1);

        Directory fromDir = directoryService.findById(fromDirId);

        return getDirMapper.toGetDirDto(
            fromDir,
            directoryService.getParentDirName(fromDir),
            directoryService.getChildNameList(fromDir),
            directoryService.getPhotoUrlList(fromDir)
        );
    }

    @Override
    @Transactional
    public GetDirDto moveFile(String fileId, String fromDirId, String toDirId) {
        setDirFileRelation(toDirId, fileId);
        deleteDirFileRelation(fromDirId, fileId);

        Directory fromDir = directoryService.findById(fromDirId);

        return getDirMapper.toGetDirDto(
            fromDir,
            directoryService.getParentDirName(fromDir),
            directoryService.getChildNameList(fromDir),
            directoryService.getPhotoUrlList(fromDir)
        );
    }

    @Override
    @Transactional
    public GetDirDto deleteOneFile(String fileId, String dirId) {
        deleteDirFileRelation(dirId, fileId);
        // deleteFile을 넘겨 줘야한다.
        trashService.saveFile(findById(fileId), dirId);
        trashBinService.saveFile(fileId);
        Directory dirDir = directoryService.findById(dirId);

        return getDirMapper.toGetDirDto(
            dirDir,
            directoryService.getParentDirName(dirDir),
            directoryService.getChildNameList(dirDir),
            directoryService.getPhotoUrlList(dirDir)
        );
    }

    @Override
    @Transactional
    public GetDirDto deleteAllFile(String fileId, String dirId) {
        return null;
    }

    @Override
    @Transactional
    public GetDirDto deleteSelectedFile(List<String> fileIdList, String dirId) {
        return null;
    }

    @Override
    @Transactional
    public void setDirFileRelation(String dirId, String fileId) {
        // dir에 fileId 추가
        Directory findDir = directoryService.findById(dirId);
        findDir.getFileIdList().add(fileId);
        directoryRepository.save(findDir);
        // file에 dirId 추가
        File findFile = findById(fileId);
        findFile.getDirIdList().add(dirId);
        fileRepository.save(findFile);
    }

    @Override
    @Transactional
    public void deleteDirFileRelation(String dirId, String fileId) {
        // dir에서 fileId 제거
        Directory findDir = directoryService.findById(dirId);
        findDir.getFileIdList().remove(fileId);
        directoryRepository.save(findDir);

        // file에 dirId 제거
        File findFile = findById(fileId);
        findFile.getDirIdList().remove(dirId);
        fileRepository.save(findFile);
    }

    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID()).append(LocalDateTime.now());
        return String.valueOf(sb);
    }

    @Override
    public File findById(String fileId) {
        return fileRepository.findById(fileId).orElseThrow(() ->
            new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));
    }
}
