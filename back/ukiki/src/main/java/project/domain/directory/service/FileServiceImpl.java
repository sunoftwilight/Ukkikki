package project.domain.directory.service;

import jakarta.transaction.TransactionScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.DataType;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.PhotoDto;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.TrashPhotoDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetFileDto;
import project.domain.directory.mapper.GetDirMapper;
import project.domain.directory.mapper.GetFileMapper;
import project.domain.directory.mapper.TrashFileMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashRepository;
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
    private final TrashBinService trashBinService;

    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final PhotoRepository photoRepository;
    private final TrashRepository trashRepository;


    @Override
    @Transactional
    public void createFile(Long partyId, Photo photo) {
        // 파티에서 rootDirId 찾기
        Party findParty = partyRepository.findById(partyId).orElseThrow(() -> new BusinessLogicException(
            ErrorCode.PARTY_NOT_FOUND));

        // file객체 생성하기
        File newFile = File.builder()
            .id(generateId())
            .photoDto(PhotoDto.builder()
                .id(photo.getId())
                .fileName(photo.getFileName())
                .photoNum(photo.getPhotoNum())
                .partyId(photo.getParty().getId())
                .memberId(photo.getMember().getId())
                .photoUrl(photo.getPhotoUrl().getPhotoUrl())
                .thumbUrl1(photo.getPhotoUrl().getThumb_url1())
                .thumbUrl2(photo.getPhotoUrl().getThumb_url2())
                .build())
            .build();

        String rootDirId = findParty.getRootDirId();
        String newFileId = newFile.getId();
        fileRepository.save(newFile);
        setDirFileRelation(rootDirId, newFileId);
    }
    @Override
    public void copyFile(String fileId, String fromDirId, String toDirId) {
        setDirFileRelation(toDirId, fileId);
        // photo num ++1
        File findFile = findById(fileId);
        Photo findPhoto = photoRepository.findById(findFile.getPhotoDto().getId())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));
        int photoNum = findPhoto.getPhotoNum();
        findPhoto.setPhotoNum(photoNum + 1);
    }

    @Override
    @Transactional
    public void copyFileList(List<String> fileIdList, String fromDirId,  String toDirId) {
        if(fileIdList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.EMPTY_FILE_ID_LIST);
        }
        for (String fileId : fileIdList) {
            copyFile(fileId, fromDirId, toDirId);
        }
    }

    @Override
    public void moveFile(String fileId, String fromDirId, String toDirId) {
        setDirFileRelation(toDirId, fileId);
        deleteDirFileRelation(fromDirId, fileId);
    }

    @Override
    @Transactional
    public void moveFileList(List<String> fileIdList, String fromDirId, String toDirId) {
        if(fileIdList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.EMPTY_FILE_ID_LIST);
        }
        for (String fileId : fileIdList) {
            moveFile(fileId, fromDirId, toDirId);
        }
    }

    @Override
    public void deleteOneFile(String fileId, String dirId) {
        // 쓰레기에 file 등록
        File file = findById(fileId);
        Trash fileTrash = saveFileToTrash(file, dirId);
        // 휴지통에 file 등록
        trashBinService.saveFileToTrashBin(fileTrash);
        // 폴더에서 제거
        deleteDirFileRelation(dirId, fileId);
        // file에서 제거
        if(file.getDirIdList().isEmpty()){
            fileRepository.deleteById(file.getId());
        }
    }

    @Override
    @Transactional
    public void deleteFileList(List<String> fileIdList, String dirId) {
        if(fileIdList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.EMPTY_FILE_ID_LIST);
        }
        for (String fileId : fileIdList) {
            deleteOneFile(fileId, dirId);
        }
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

    @Override
    public String getFile(String fileId) {
        File file = findById(fileId);
        return file.getPhotoDto().getPhotoUrl();

    }

    @Override
    public Trash saveFileToTrash(File file, String dirId) {
        // file에 trashId와 삭제 당시 dirid 추가
        return trashRepository.save(Trash.builder()
            .id(generateId())
            .dataType(DataType.FILE)
            .rawId(file.getId())
            .content(TrashFileDto.builder()
                .id(file.getId())
                .photoDto(file.getPhotoDto())
                .dirId(dirId)
                .build())
            .deadLine(LocalDate.now().plusWeeks(2))
            .fullRoot(directoryService.getFullRootByDirId(dirId))
            .build());
    }
}
