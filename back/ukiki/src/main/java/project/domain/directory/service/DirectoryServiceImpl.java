package project.domain.directory.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.request.MoveDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.DirWithChildsNameDto;
import project.domain.directory.dto.response.RenameDirDto;
import project.domain.directory.mapper.DirMapper;
import project.domain.directory.mapper.DirWithChildsNameMapper;
import project.domain.directory.mapper.RenameDirMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.TrashRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class DirectoryServiceImpl implements DirectoryService {


    private final TrashService trashService;
    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final TrashRepository trashRepository;
    private final DirMapper dirMapper;
    private final DirWithChildsNameMapper dirWithChildsNameMapper;
    private final RenameDirMapper renameDirMapper;

    @Override
    public DirWithChildsNameDto getDir(String dirId) {
        Directory dir = findById(dirId);
        return dirWithChildsNameMapper.toDirWithChildsNameDto(dir, getChildNameList(dir));
    }

    @Override
    @Transactional
    public DirWithChildsNameDto createDir(CreateDirDto request) {
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

        return dirWithChildsNameMapper.toDirWithChildsNameDto(parentDir, getChildNameList(parentDir));
    }

    @Override
    @Transactional
    public DirWithChildsNameDto moveDir(MoveDirDto request) {
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
        return dirWithChildsNameMapper.toDirWithChildsNameDto(fromDir, getChildNameList(fromDir));
    }

    @Override
    @Transactional
    public DirWithChildsNameDto deleteDir(String dirId) {
        // 부모의 child list에서 해당 dirId 제거
        Directory dir = findById(dirId);
        Directory parentDir = findById(dir.getParentDirId());
        parentDir.getChildDirIdList().remove(dirId);
        directoryRepository.save(parentDir);
        // child 는 그대로 휴지통으로 임시 저장
        trashService.save(dir);
        // child 삭제
        directoryRepository.deleteById(dirId);

        return dirWithChildsNameMapper.toDirWithChildsNameDto(parentDir, getChildNameList(parentDir));
    }

    @Override
    @Transactional
    public DirWithChildsNameDto restoreDir(String deletedDirId) {
        // 뒤진 놈 가저오기
        Trash deletedDir = trashService.findById(deletedDirId);
        // 오늘이 deadLine보다 크다면 복구 불가능
        if(trashService.isOutOfRecoveryPeriod(deletedDir)) {
            throw new BusinessLogicException(ErrorCode.DIRECTORY_OUT_OF_DEADLINE);
        }

        // 새롭게 태어나기
        Directory restoredDir = Directory.builder()
            .id(deletedDir.getDirectoryId())
            .dirName(deletedDir.getDirName())
            .parentDirId(deletedDir.getParentDirId())
            .childDirIdList(deletedDir.getChildDirIdList())
            .photoList(deletedDir.getPhotoList())
            .build();
        // 부모에 이어붙여주기 => 부모가 directory에 있으면 진행 없으면 찾기
        Directory parentDir = findById(deletedDir.getParentDirId());
        parentDir.getChildDirIdList().add(deletedDir.getDirectoryId());
        // 저장하기
        directoryRepository.saveAll(toList(restoredDir, parentDir));
        // 기존 휴지통에 있었던 그놈 삭제하기
        trashRepository.delete(deletedDir);
        return dirWithChildsNameMapper.toDirWithChildsNameDto(restoredDir, getChildNameList(restoredDir));
    }

    @Override
    @Transactional
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
    @Transactional
    public DirDto initDirParty(Long partyId) {
        Party findParty = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 만약 party에 이미 rootDir이 있다면 나가리
        if(findParty.getRootDirId() != null) {
            throw new BusinessLogicException(ErrorCode.PARTY_ALREADY_HAVE_ROOT_DIR);
        }

        // 기본 폴더 생성 및 저장
        Directory rootDir = directoryRepository.save(Directory.builder()
            .id(generateId())
            .dirName("root")
            .parentDirId(null)
            .build());
        // party에 rootDirId 저장
        findParty.setRootDirId(rootDir.getId());
        return dirMapper.toDirDto(rootDir);
    }

    @Override
    public List<Directory> toList(Directory... directories) {
        return new ArrayList<>(Arrays.asList(directories));
    }

    @Override
    public List<String> getChildNameList(Directory dir) {
        List<String> childNameList = new ArrayList<>();
        List<String> childIdList = dir.getChildDirIdList();
        for (String childId : childIdList) {
            // child 가져와서 이름 채우기
            Directory childDir = findById(childId);
            childNameList.add(childDir.getDirName());
        }

        return childNameList;
    }

}
