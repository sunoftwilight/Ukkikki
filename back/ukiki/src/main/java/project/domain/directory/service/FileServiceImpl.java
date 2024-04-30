package project.domain.directory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService{

    private final DirectoryService directoryService;
    private final TrashService trashService;
    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;


    @Override
    @Transactional
    public void createFile(Long partyId, Long photoId) {
        // file객체 생성하기
        File newFile = File.builder()
            .fileId(generateId())
            .photoId(photoId)
            .build();

        // 파티에서 rootDirId 찾기
        Party findParty = partyRepository.findById(partyId).orElseThrow(() -> new BusinessLogicException(
            ErrorCode.PARTY_NOT_FOUND));

        String rootDirId = findParty.getRootDirId();
        String newFileId = newFile.getFileId();
        setDirFileRelation(rootDirId, newFileId);
    }

    @Override
    public void copyFile(String targetDirId, String fileId) {
        setDirFileRelation(targetDirId, fileId);
    }

    @Override
    public void moveFile(String fromDirId, String toDirId, String fileId) {
        setDirFileRelation(toDirId, fileId);
        deleteDirFileRelation(fromDirId, fileId);
    }

    @Override
    public void deleteOneFile(String dirId, String fileId) {
        deleteDirFileRelation(dirId, fileId);
        trashService.saveFile(findById(fileId));
    }

    @Override
    public void deleteAllFile(String fileId) {

    }

    @Override
    public void deleteSelectedFile(List<String> fileIdList) {

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
}
