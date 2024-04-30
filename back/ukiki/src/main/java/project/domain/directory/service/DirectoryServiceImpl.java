package project.domain.directory.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.request.MoveDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.RenameDirDto;
import project.domain.directory.mapper.DirMapper;
import project.domain.directory.mapper.RenameDirMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

//@Id
//private String id;
//private String dirName;
//private String parentDirId;
//@Builder.Default
//private List<String> childDirIdList = new ArrayList<>();
//@Builder.Default
//private List<Long> photoList = new ArrayList<>();
@Service
@AllArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {


    private final TrashService trashService;
    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final DirMapper dirMapper;
    private final RenameDirMapper renameDirMapper;

    @Override
    public DirDto getDir(String dirId) {
        return dirMapper.toDirDto(findById(dirId));
    }

    @Override
    public DirDto createDir(CreateDirDto request) {
        // 새로운 dir 생성
        Directory childDir = Directory.builder()
            .id(generateId())
            .dirName(request.getDirName())
            .parentDirId(request.getParentDirId())
            .build();
        // parentDir에 새로운 dir 추가
        Directory parentDir = findById(request.getParentDirId());
        parentDir.getChildDirIdList().add(childDir.getId());

        directoryRepository.saveAll(toList(childDir, parentDir));

        return dirMapper.toDirDto(parentDir);
    }

    @Override
    public DirDto moveDir(MoveDirDto request) {
        String dirId = request.getDirId();
        String toDirId = request.getToDirId();

        Directory dir = findById(dirId);
        Directory fromDir = findById(dir.getParentDirId());
        Directory toDir = findById(toDirId);
        // fromDirId : 자식 리스트에서 dirId 제거
        fromDir.getChildDirIdList().remove(dirId);
        // toDirId : 자식 리스트에 dirId 추가
        toDir.getChildDirIdList().add(dirId);
        // dirId : 부로를 toDirId로 수정
        dir.setParentDirId(toDirId);

        directoryRepository.saveAll(toList(dir, fromDir, toDir));
        return dirMapper.toDirDto(fromDir);
    }

    @Override
    public DirDto deleteDir(String dirId) {
        // 부모의 child list에서 해당 dirId 제거
        Directory dir = findById(dirId);
        Directory parentDir = findById(dir.getParentDirId());
        parentDir.getChildDirIdList().remove(dirId);
        directoryRepository.save(parentDir);
        // child 는 그대로 휴지통으로 임시 저장
        trashService.save(dir);
        // child 삭제
        directoryRepository.deleteById(dirId);

        return dirMapper.toDirDto(parentDir);
    }

    @Override
    public DirDto restoreDir(String deletedDirId) {
        // 뒤진 놈 가저오기
        Trash deletedDir = trashService.findById(deletedDirId);
        // 새롭게 태어나기
        Directory restoredDir = Directory.builder()
            .id(deletedDir.getDirectoryId())
            .dirName(deletedDir.getDirName())
            .parentDirId(deletedDir.getParentDirId())
            .childDirIdList(deletedDir.getChildDirIdList())
            .photoList(deletedDir.getPhotoList())
            .build();
        // 뒤진놈의 엄마에 이어붙여주기 => 뒤진놈의 엄마가 directory에 있으면 진행 없으면 찾기
        Directory parentDir = findById(deletedDir.getParentDirId());
        parentDir.getChildDirIdList().add(deletedDir.getDirectoryId());
        // 저장하기
        directoryRepository.saveAll(toList(restoredDir, parentDir));
        return dirMapper.toDirDto(restoredDir);
    }

    @Override
    public RenameDirDto renameDir(project.domain.directory.dto.request.RenameDirDto request) {
        String dirId = request.getDirId();
        String newName = request.getNewName();
        Directory dir = findById(dirId);
        dir.setDirName(newName);
        directoryRepository.save(dir);
        return renameDirMapper.toRenameDirDto(dir);
    }

    @Override
    public Directory findById(String dirId) {
        return directoryRepository.findById(dirId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
    }

    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID()).append(LocalDateTime.now());
        return String.valueOf(sb);
    }

    @Override
    public DirDto initDirParty(Long partyId) {
        Optional<Party> findParty = partyRepository.findById(partyId);
        // 기본 폴더 생성 및 저장
        Directory rootDir = directoryRepository.save(Directory.builder()
            .id(generateId())
            .dirName("root")
            .parentDirId(null)
            .build());
        return dirMapper.toDirDto(rootDir);
    }

    @Override
    public List<Directory> toList(Directory... directories) {
        return new ArrayList<>(Arrays.asList(directories));
    }
}
