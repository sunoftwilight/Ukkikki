package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetDirDtov2;
import project.domain.directory.dto.response.GetDirInnerDtov2;
import project.domain.directory.dto.response.GetDirListDto;
import project.domain.directory.dto.response.RenameDirDto;
import project.domain.directory.mapper.DirMapper;
import project.domain.directory.mapper.GetDirMapper;
import project.domain.directory.mapper.RenameDirMapper;
import project.domain.directory.mapper.TrashFileMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.directory.repository.TrashRepository;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class DirectoryServiceImpl implements DirectoryService {

    private static Deque<Directory> deque = new ArrayDeque<>();
    private static HashSet<String> visitedSet = new HashSet<>();
    private static ModelMapper modelMapper;

    private final FileRepository fileRepository;
    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final TrashRepository trashRepository;
    private final TrashBinRepository trashBinRepository;
    private final MemberRepository memberRepository;
    private final MemberpartyRepository memberpartyRepository;

    private final DirMapper dirMapper;
    private final RenameDirMapper renameDirMapper;
    private final GetDirMapper getDirMapper;
    private final TrashFileMapper trashFileMapper;

    @Override
    public List<GetDirListDto> getDirList(Long userId) {
        // 유저 찾기
        Member member = memberRepository.findById(userId).orElseThrow(
            () -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND)
        );
        // 해당 유저의 즐겨찾기 지정 폴더
        String mainDirId = member.getMainDirId();
        // 해당 유저가 포함된 모든 party 찾기
        List<MemberParty> memberPartyList = memberpartyRepository.findMemberPartiesByMember(
            member);

        // 유저의 보유 방 리스트
        List<GetDirListDto> response = new ArrayList<>();
        for (MemberParty memberParty : memberPartyList) {
            // party 구하기
            Party party = partyRepository.findById(memberParty.getParty().getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
            // directory 구하기
            String rootDirId = party.getRootDirId();
            Directory directory = directoryRepository.findById(rootDirId).orElseThrow(
                () -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND)
            );
            // 반환 Dto 생성하기
            GetDirListDto getDirListDto = GetDirListDto.builder()
                .pk(party.getRootDirId())
                .name(directory.getDirName())
                .thumbnail(party.getThumbnail())
                .isStar(directory.getId().equals(mainDirId))
                .build();
            // 결과 리스트에 넣어주기
            response.add(getDirListDto);
        }
        return response;
    }

    @Override
    public GetDirDto getDir(String dirId) {
        Directory dir = findById(dirId);
        return getDirMapper.toGetDirDto(
            dir,
            getParentDirName(dir),
            getChildNameList(dir),
            getPhotoUrlList(dir)
            );
    }

    @Override
    public GetDirDtov2 getDirv2(String dirId) {
        Directory dir = findById(dirId);

        GetDirDtov2 getDirDtov2 = GetDirDtov2.builder()
            .parentId(dir.getParentDirId())
            .build();
        List<GetDirInnerDtov2> contentList = getDirDtov2.getContentList();

        // 우선 폴더부터 채우기
        List<String> childDirIdList = dir.getChildDirIdList();
        // childDir
        if(!childDirIdList.isEmpty()) {
            for (Directory childDir : directoryRepository.findAllById(childDirIdList)) {
                GetDirInnerDtov2 dirTypeDto = GetDirInnerDtov2.builder()
                    .type(DataType.DIRECTORY)
                    .pk(childDir.getId())
                    .name(childDir.getDirName())
                    .url("None")
                    .build();
                contentList.add(dirTypeDto);
            }
        }

        // 사진 채우기
        List<String> fileIdList = dir.getFileIdList();
        if(!fileIdList.isEmpty()) {
            List<File> fileList = fileRepository.findAllById(fileIdList);
            // 썸네일을 줘야됨
            for (File file : fileList) {
                // json to Photo
                Photo photo = modelMapper.map(file.getPhoto(), Photo.class);
                // json to PhotoUrl
                PhotoUrl photoUrl = modelMapper.map(photo.getPhotoUrl(), PhotoUrl.class);
                GetDirInnerDtov2 fileType = GetDirInnerDtov2.builder()
                    .type(DataType.FILE)
                    .pk(file.getId())
                    .name("None")
                    .url(photoUrl.getThumb_url1())
                    .build();
                contentList.add(fileType);
            }
        }
        return getDirDtov2;
    }


    @Override
    @Transactional
    public GetDirDto createDir(CreateDirDto request) {
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

        return getDirMapper.toGetDirDto(
            parentDir,
            getParentDirName(parentDir),
            getChildNameList(parentDir),
            getPhotoUrlList(parentDir)
        );
    }

    @Override
    @Transactional
    public GetDirDto moveDir(String dirId, String toDirId) {
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
        return getDirMapper.toGetDirDto(
            fromDir,
            getParentDirName(fromDir),
            getChildNameList(fromDir),
            getPhotoUrlList(fromDir)
        );
    }

    @Override
    @Transactional
    public GetDirDto deleteDir(String dirId) {  // photo의 경우도 고려해줘야한다.
        // 부모의 child list에서 해당 dirId 제거, child에서 parent는 제거하지 않음
        Directory dir = findById(dirId);
        Directory parentDir = findById(dir.getParentDirId());
        parentDir.getChildDirIdList().remove(dirId);
        directoryRepository.save(parentDir);

        // 대표 폴더만 TrashBin에 저장, 자식 폴더나 사진 파일은 Trash 로 저장
        saveDirInTrashBin(dir);

        // 초기화 작업
        deque.push(dir);
        visitedSet.add(dir.getId());
        while (!deque.isEmpty()) {
            // pop + 할 거 하기
            Directory curDir = deque.pop();
            // 현재 폴더 휴지통에 저장
            saveDirtoTrash(curDir);
            // 현재 폴더의 파일 휴지통으로 이동 및 폴더에서 삭제
            List<File> fileList = fileRepository.findAllById(curDir.getFileIdList());
            for (File file : fileList) {
                saveFileToTrash(file, curDir.getId());
                fileRepository.delete(file);
            }
            // 현재 파일 폴더에서 삭제
            directoryRepository.delete(curDir);
            // child 탐색
            // childIdList가 null이면 continue
            if (curDir.getFileIdList().isEmpty()) {
                continue;
            }
            // childIsList가 null이 아닌경우 탐색
            for (String nextDirId : curDir.getChildDirIdList()) {
                if(visitedSet.contains(nextDirId)){
                    continue;
                }
                Directory nextDir = directoryRepository.findById(nextDirId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
                // push
                deque.push(nextDir);
                // 방문 체크
                visitedSet.add(nextDirId);
            }
        }

        return getDirMapper.toGetDirDto(
            parentDir,
            getParentDirName(parentDir),
            getChildNameList(parentDir),
            getPhotoUrlList(parentDir)
        );
    }

    @Override
    @Transactional
    public RenameDirDto renameDir(String dirId, String newName) {
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
    public DirDto initDirPartyTest(Long partyId) {
        Party findParty = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 만약 party에 이미 rootDir이 있다면 나가리
        if(findParty.getRootDirId() != null) {
            throw new BusinessLogicException(ErrorCode.PARTY_ALREADY_HAVE_ROOT_DIR);
        }

        // 기본 폴더 생성 및 저장
        Directory rootDir = directoryRepository.save(
            Directory.builder()
                .id(generateId())
                .dirName("root")
                .parentDirId("")
                .build());
        // party에 rootDirId 저장
        findParty.setRootDirId(rootDir.getId());
        return dirMapper.toDirDto(rootDir);
    }

    @Override
    @Transactional
    public void initDirParty(Party party) {
        // 만약 party에 이미 rootDir이 있다면 나가리
        if(party.getRootDirId() != null) {
            throw new BusinessLogicException(ErrorCode.PARTY_ALREADY_HAVE_ROOT_DIR);
        }

        // 기본 폴더 생성 및 저장
        Directory rootDir = directoryRepository.save(
            Directory.builder()
                .id(generateId())
                .dirName("root")
                .parentDirId("")
                .build());
        // party에 rootDirId 저장
        party.setRootDirId(rootDir.getId());
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

    @Override
    public String getParentDirName(Directory directory) {
        if (Objects.equals(directory.getParentDirId(), "")) {
            return "";  // 또는 ""로 반환
        }
        log.info("여기");
        Directory parentDir = findById(directory.getParentDirId());
        return parentDir != null ? parentDir.getDirName() : "No Parent";
    }

    @Override
    public List<String> getChildDirNameList(Directory directory) {
        List<String> childDirNameList = new ArrayList<>();
        List<Directory> childDirList = directoryRepository.findAllById(directory.getChildDirIdList());
        for (Directory childDir : childDirList) {
            childDirNameList.add(childDir.getDirName());
        }

        return childDirNameList;
    }

    @Override
    public List<String> getPhotoUrlList(Directory directory) {
        List<String> photoUrlList = new ArrayList<>();
        List<File> FileList =  fileRepository.findAllById(directory.getFileIdList());
        ModelMapper modelMapper = new ModelMapper();
        for (File file : FileList) {
            Photo photo = modelMapper.map(file.getPhoto(), Photo.class);
            photoUrlList.add(photo.getPhotoUrl().getThumb_url1());
        }
        return photoUrlList;
    }

    @Override
    public String getRootDirId(Directory dir) {
        int cnt = 0;
        while(!dir.getParentDirId().equals("")) {
            dir = findById(dir.getParentDirId());
            cnt++;
            if(cnt > 100){
                throw new BusinessLogicException(ErrorCode.ROOTDIR_NOT_FOUND);
            }
        }
        return dir.getId();
    }

    public Trash saveDirtoTrash(Directory dir) {
        return trashRepository.save(Trash.builder()
            .id(generateId())
            .rawId(dir.getId())
            .dataType(DataType.DIRECTORY)
            .content(dir)
            .deadLine(LocalDate.now().plusWeeks(2))
            .build());
    }


    public void saveDirInTrashBin(Directory dir) {
        // parentDirId = ""이면 while 탈출
        String rootDirId = getRootDirId(dir);
        Party party = partyRepository.findPartyByRootDirId(rootDirId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        Long partyId = party.getId();
        TrashBin trashBin = trashBinRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getDirIdList().add(dir.getId());
        trashBinRepository.save(trashBin);
    }

    @Override
    @Transactional
    public Trash saveFileToTrash(File file, String dirId) {
        // file to deleteFile
        TrashFileDto trashFileDto = trashFileMapper.toTrashFile(file, dirId);
        return trashRepository.save(Trash.builder()
            .id(generateId())
            .rawId(trashFileDto.getId())
            .dataType(DataType.FILE)
            .content(trashFileDto)
            .deadLine(LocalDate.now().plusWeeks(2))
            .build());
    }
}
