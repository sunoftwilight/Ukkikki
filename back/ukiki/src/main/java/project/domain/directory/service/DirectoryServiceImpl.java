package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
import project.domain.directory.dto.response.GetChildDirDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetDirDtov2;
import project.domain.directory.dto.response.GetDirFullStructureDto;
import project.domain.directory.dto.response.GetDirInnerDtov2;
import project.domain.directory.dto.response.GetDirListDto;
import project.domain.directory.dto.response.GetDirThumbUrl2;
import project.domain.directory.mapper.DirMapper;
import project.domain.directory.mapper.GetDirMapper;
import project.domain.directory.mapper.RenameDirMapper;
import project.domain.directory.mapper.TrashFileMapper;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.directory.repository.TrashRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.member.repository.KeyGroupRepository;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.entity.Face;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.mediatable.DownloadLog;
import project.domain.photo.entity.mediatable.Likes;
import project.domain.photo.repository.DownloadLogRepository;
import project.domain.photo.repository.FaceRepository;
import project.domain.photo.repository.LikesRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.S3Util;

@Service
@AllArgsConstructor
@Slf4j
public class DirectoryServiceImpl implements DirectoryService {

    private static final Deque<Directory> deque = new ArrayDeque<>();
    private static final HashSet<String> visitedSet = new HashSet<>();

    private final TrashBinService trashBinService;
    private final FileRepository fileRepository;
    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final TrashRepository trashRepository;
    private final TrashBinRepository trashBinRepository;
    private final MemberRepository memberRepository;
    private final MemberpartyRepository memberpartyRepository;
    private final PhotoRepository photoRepository;
    private final LikesRepository likesRepository;
    private final DownloadLogRepository downloadLogRepository;
    private final FaceRepository faceRepository;
    private final KeyGroupRepository keyGroupRepository;
    private final S3Util s3Util;

    private final DirMapper dirMapper;
    private final RenameDirMapper renameDirMapper;
    private final GetDirMapper getDirMapper;
    private final TrashFileMapper trashFileMapper;


    @Override
    public List<GetChildDirDto> getChildDir(String dirId) {
        log.info("come in Service");
        List<GetChildDirDto> response = new ArrayList<>();
        Directory directory = directoryRepository.findById(dirId).orElseThrow(
            () -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
        List<String> childDirIdList = directory.getChildDirIdList();
        log.info("childDirIdList = {}", childDirIdList);
        if (childDirIdList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.NO_MORE_CHILD_DIR);
        }
        for (Directory childDir : directoryRepository.findAllById(childDirIdList)) {
            response.add(GetChildDirDto.builder()
                .pk(childDir.getId())
                .name(childDir.getDirName())
                .build());
        }
        log.info("service response = {}", response);
        return response;
    }

    @Override
    public List<GetDirFullStructureDto> getDirFullStructure(String dirId) {
        Directory dir = findById(dirId);

        // 최상단 rootDirId 검색
        String rootDirId = getRootDirId(dir);
        // DFS
        return dfs_getDirFullStructure(rootDirId);

    }

    public List<GetDirFullStructureDto> dfs_getDirFullStructure(String rootDirId) {
        List<GetDirFullStructureDto> result = new ArrayList<>();
        Directory dir = findById(rootDirId);

        // 초기화
        HashMap<String, Integer> dirDepth = new HashMap<>();
        deque.push(dir);
        visitedSet.add(dir.getId());
        dirDepth.put(dir.getId(), 0);

        while (!deque.isEmpty()) {
            // pop + 만들기
            Directory curDir = deque.pop();
            result.add(
                GetDirFullStructureDto.builder()
                    .depth(dirDepth.get(curDir.getId()))
                    .name(curDir.getDirName())
                    .pk(curDir.getId())
                    .build()
            );

            List<String> childDirIdList = curDir.getChildDirIdList();
            if (childDirIdList.isEmpty()) {
                continue;
            }
            // 탐색

            for (Directory nextDir : directoryRepository.findAllById(childDirIdList)) {
                // 유효성 검사
                if (visitedSet.contains(nextDir.getId())) {
                    continue;
                }
                // push
                deque.push(nextDir);
                visitedSet.add(nextDir.getId());
                dirDepth.put(nextDir.getId(), dirDepth.get(curDir.getId()) + 1);
            }

        }
        visitedSet.clear();
        return result;
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
        // member 찾기(다운 여부, 좋아요 여부 찾을때 사용)
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        Directory dir = findById(dirId);

        GetDirDtov2 getDirDtov2 = GetDirDtov2.builder()
            .parentId(dir.getParentDirId())
            .build();
        List<GetDirInnerDtov2> contentList = getDirDtov2.getContentList();

        // 우선 폴더부터 채우기
        List<String> childDirIdList = dir.getChildDirIdList();
        // childDir
        if (!childDirIdList.isEmpty()) {
            for (Directory childDir : directoryRepository.findAllById(childDirIdList)) {
                GetDirInnerDtov2 dirTypeDto = GetDirInnerDtov2.builder()
                    .type(DataType.DIRECTORY)
                    .pk(childDir.getId())
                    .name(childDir.getDirName())
                    .build();
                contentList.add(dirTypeDto);
            }
        }

        // 사진 채우기
        List<String> fileIdList = dir.getFileIdList();
        if (!fileIdList.isEmpty()) {
            List<File> fileList = fileRepository.findAllById(fileIdList);
            // 썸네일을 줘야됨
            for (File file : fileList) {
                Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

                Optional<DownloadLog> opDownloadLog = downloadLogRepository.findByMemberAndPhoto(
                    member, photo);
                Optional<Likes> opLikes = likesRepository.findByMemberAndPhoto(member,
                    photo);

                GetDirInnerDtov2 fileType = GetDirInnerDtov2.builder()
                    .type(DataType.FILE)
                    .pk(file.getId())
                    .photoId(file.getPhotoDto().getId())
                    .url(file.getPhotoDto().getThumbUrl1())
                    .isDownload(opDownloadLog.isPresent())
                    .isLikes(opLikes.isPresent())
                    .build();
                contentList.add(fileType);
            }
        }
        return getDirDtov2;
    }


    @Override
    @Transactional
    public void createDir(CreateDirDto request) {
        if (!isValidRole(request.getParentDirId(), MemberRole.EDITOR, MemberRole.MASTER)) {
            throw new BusinessLogicException(ErrorCode.INVALID_MEMBER_ROLE);
        }


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
    }

    @Override
    @Transactional
    public void moveDir(String dirId, String toDirId) {
        if (!isValidRole(dirId, MemberRole.EDITOR, MemberRole.MASTER)) {
            throw new BusinessLogicException(ErrorCode.INVALID_MEMBER_ROLE);
        }

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
    }

    //    @Override
//    @Transactional
//    public void deleteDir(String dirId) {  // photo의 경우도 고려해줘야한다.
//        // 폴더에서 제거, file에서 제거, 휴지통에 저장, 쓰레기 등록
//
//        Directory dir = findById(dirId);
//        if (dir.getParentDirId().equals("")) {
//            throw new BusinessLogicException(ErrorCode.FIND_PARENT_OF_ROOT_NOT_AVAILABLE);
//        }
//
//        Directory parentDir = findById(dir.getParentDirId());
//        parentDir.getChildDirIdList().remove(dirId);
//        directoryRepository.save(parentDir);
//
//        // 초기화 작업
//        deque.push(dir);
//        visitedSet.add(dir.getId());
//        while (!deque.isEmpty()) {
//            // pop
//            Directory curDir = deque.pop();
//            // 현재 폴더 휴지통에 저장(폴더에 남아있는 상황)
//            saveDirtoTrash(curDir);
//            // 현재 폴더의 파일 휴지통에 저장후 폴더에서 삭제
//            // fileList가 null이 아닐 경우에만 작업
//            if (!curDir.getFileIdList().isEmpty()) {
//                List<File> fileList = fileRepository.findAllById(curDir.getFileIdList());
//                for (File file : fileList) {
//                    saveFileToTrash(file, curDir.getId());
//                    // 사진의 DirIdList 에서 curDirId 제외하기
//                    file.getDirIdList().remove(curDir.getId());
//                    fileRepository.save(file);
//                    // 사진이 이제 전체 폴더에 존재하지 않는 경우에 삭제
//                    if(file.getDirIdList().isEmpty()) {
//                        fileRepository.delete(file);
//                        fileRepository.save(file);
//                    }
//                }
//            }
//            // curDir의 자식 폴더 탐색
//            // childIdList가 null면 현재 폴더 지우고 다음 deque의 dir 탐색
//            if (curDir.getChildDirIdList().isEmpty()) {
//                directoryRepository.delete(curDir);
//                continue;
//            }
//            // childIsList가 null이 아닌경우 탐색
//            for (String nextDirId : curDir.getChildDirIdList()) {
//                if(visitedSet.contains(nextDirId)){
//                    continue;
//                }
//                Directory nextDir = directoryRepository.findById(nextDirId)
//                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
//                // push
//                deque.push(nextDir);
//                // 방문 체크
//                visitedSet.add(nextDirId);
//            }
//            // 탐색 끝난 폴더 삭제
//            directoryRepository.delete(curDir);
//        }
//        // 다음 요청 수행을 위해 visitedSet 비워주기
//        visitedSet.clear();
//
//        // 해당 휴지통에 삭제된 dirTrashId 추가
//        Trash dirTrash = trashRepository.findFirstByRawId(dir.getId())
//            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));
//        Long trashBinId = getTrashBinId(dir);
//        TrashBin trashBin = trashBinRepository.findById(trashBinId)
//            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
//        trashBin.getDirTrashIdList().add(dirTrash.getId());
//        trashBinRepository.save(trashBin);
//    }
    @Override
    @Transactional
    public void deleteDir(String dirId, String sseKey) { // photo의 경우도 고려해줘야 한다.
        if (!isValidRole(dirId, MemberRole.EDITOR, MemberRole.MASTER)) {
            throw new BusinessLogicException(ErrorCode.INVALID_MEMBER_ROLE);
        }

        // 루트 폴더에서 부모 폴더를 찾는 것은 불가능합니다.
        Directory dir = findById(dirId);
        if (dir.getParentDirId().equals("")) {
            throw new BusinessLogicException(ErrorCode.FIND_PARENT_OF_ROOT_NOT_AVAILABLE);
        }

        // 부모 폴더에서 이 폴더를 제거
        Directory parentDir = findById(dir.getParentDirId());
        parentDir.getChildDirIdList().remove(dirId);
        directoryRepository.save(parentDir);

        // 모든 폴더와 파일을 순회하고 후위 탐색 방식으로 삭제
        postOrderDelete(dir, sseKey);

        // 다음 요청을 위해 visitedSet 비워주기
        visitedSet.clear();

        // 휴지통에 삭제된 dirTrashId 추가
        Trash dirTrash = trashRepository.findFirstByRawId(dirId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));
        Long trashBinId = getTrashBinId(dir);
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getDirTrashIdList().add(dirTrash.getId());
        trashBinRepository.save(trashBin);
    }

    private void postOrderDelete(Directory curDir, String sseKey) {
        // 현재 디렉토리를 이미 방문한 경우 패스
        if (visitedSet.contains(curDir.getId())) {
            return;
        }

        // 방문 기록에 추가
        visitedSet.add(curDir.getId());

        // 자식 디렉토리를 재귀적으로 삭제
        for (String nextDirId : curDir.getChildDirIdList()) {
            Directory nextDir = directoryRepository.findById(nextDirId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
            postOrderDelete(nextDir, sseKey);
        }

        // 현재 디렉토리의 파일을 휴지통에 저장하고 삭제
        if (!curDir.getFileIdList().isEmpty()) {
            List<File> fileList = fileRepository.findAllById(curDir.getFileIdList());
            for (File file : fileList) {
                saveFileToTrash(file, curDir.getId());
                file.getDirIdList().remove(curDir.getId());
                fileRepository.save(file);
                if (file.getDirIdList().isEmpty()) {
                    //=== s3 만료일 설정 시작
                    Long photoId = file.getPhotoDto().getId();
                    Photo photo = photoRepository.findById(photoId)
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

                    // 인물 분류 사진 만료일 설정
                    List<Face> faceList = faceRepository.findByOriginImageUrl(
                        photo.getPhotoUrl().getPhotoUrl());
                    for (Face face : faceList) {
                        String url = face.getFaceImageUrl();
                        String fileName = url.split("/")[3];
                        s3Util.fileExpire(sseKey, fileName);
                    }
                    // 썸네일 사진 만료일 설정
                    for (String url : photo.getPhotoUrl().photoUrls()) {
                        String fileName = url.split("/")[3];
                        s3Util.fileExpire(sseKey, fileName);
                    }
                    // 원본 사진 만료일 설정
                    s3Util.fileExpire(sseKey, photo.getFileName());
                    //=== s3 만료일 설정종료

                    fileRepository.delete(file);
                }
            }
        }

        // 현재 디렉토리를 휴지통에 저장하고 삭제
        saveDirtoTrash(curDir);
        directoryRepository.delete(curDir);
    }

    @Override
    @Transactional
    public void renameDir(String dirId, String newName) {
        Directory dir = findById(dirId);
        dir.setDirName(newName);
        directoryRepository.save(dir);
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
        if (findParty.getRootDirId() != null) {
            throw new BusinessLogicException(ErrorCode.PARTY_ALREADY_HAVE_ROOT_DIR);
        }

        // 기본 폴더 생성 및 저장
        Directory rootDir = directoryRepository.save(
            Directory.builder()
                .id(generateId())
                .dirName("root")
                .parentDirId("")
                .build());
        trashBinService.createTrashBinTest(1L);

        // party에 rootDirId 저장
        findParty.setRootDirId(rootDir.getId());
        return dirMapper.toDirDto(rootDir);
    }

    @Override
    @Transactional
    public void initDirParty(Party party) {
        // 만약 party에 이미 rootDir이 있다면 나가리
        if (party.getRootDirId() != null) {
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
        log.info("상위 폴더가 있는 경우");
        Directory parentDir = findById(directory.getParentDirId());
        return parentDir != null ? parentDir.getDirName() : "No Parent";
    }

    @Override
    public List<String> getChildDirNameList(Directory directory) {
        List<String> childDirNameList = new ArrayList<>();
        List<Directory> childDirList = directoryRepository.findAllById(
            directory.getChildDirIdList());
        for (Directory childDir : childDirList) {
            childDirNameList.add(childDir.getDirName());
        }

        return childDirNameList;
    }

    @Override
    public List<String> getPhotoUrlList(Directory directory) {
        List<String> photoUrlList = new ArrayList<>();
        List<File> FileList = fileRepository.findAllById(directory.getFileIdList());
        for (File file : FileList) {
            photoUrlList.add(file.getPhotoDto().getThumbUrl1());
        }
        return photoUrlList;
    }

    @Override
    public String getRootDirId(Directory dir) {
        int cnt = 0;
        while (!dir.getParentDirId().equals("")) {
            dir = findById(dir.getParentDirId());
            cnt++;
            if (cnt > 100) {
                throw new BusinessLogicException(ErrorCode.ROOTDIR_NOT_FOUND);
            }
        }
        return dir.getId();
    }

    public List<String> getFullRootByDirId(String dirId) {
        Deque<String> dequeDirId = new ArrayDeque<>();

        dequeDirId.addFirst(dirId);
        Directory dir = findById(dirId);
        int cnt = 0;
        while (!dir.getParentDirId().equals("")) {
            dir = findById(dir.getParentDirId());
            dequeDirId.addFirst(dir.getId());
            cnt++;
            if (cnt > 100) {
                throw new BusinessLogicException(ErrorCode.ROOTDIR_NOT_FOUND);
            }
        }
        return dequeDirId.stream().toList();
    }

    @Override
    public List<String> getFullNameByDirId(String dirId) {
        Deque<String> dequeName = new ArrayDeque<>();

        Directory dir = findById(dirId);
        dequeName.addFirst(dir.getDirName());
        int cnt = 0;
        while (!dir.getParentDirId().equals("")) {
            dir = findById(dir.getParentDirId());
            dequeName.addFirst(dir.getDirName());
            cnt++;
            if (cnt > 100) {
                throw new BusinessLogicException(ErrorCode.ROOTDIR_NOT_FOUND);
            }
        }
        return dequeName.stream().toList();
    }

    public Trash saveDirtoTrash(Directory dir) {
        return trashRepository.save(Trash.builder()
            .id(generateId())
            .rawId(dir.getId())
            .dataType(DataType.DIRECTORY)
            .content(dir)
            .deadLine(LocalDate.now().plusWeeks(2))
            .fullRout(getFullRootByDirId(dir.getParentDirId()))
            .fullName(getFullNameByDirId(dir.getParentDirId()))
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
        trashBin.getDirTrashIdList().add(dir.getId());
        trashBinRepository.save(trashBin);
    }

    public Long getTrashBinId(Directory dir) {
        String rootDirId = getRootDirId(dir);
        Party party = partyRepository.findPartyByRootDirId(rootDirId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 파티Id가 곧 TrashBin의 Id
        Long partyId = party.getId();
        return partyId;
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
            .build());
    }

    @Override
    public List<GetDirThumbUrl2> getDirThumbUrl2(String dirId) {
        log.info("come in service");
        Directory dir = findById(dirId);
        List<GetDirThumbUrl2> response = new ArrayList<>();
        List<String> fileIdList = dir.getFileIdList();
        log.info("fileIdList = {}", fileIdList);
        if (fileIdList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.FILE_NOT_FOUND);
        }
        for (File file : fileRepository.findAllById(fileIdList)) {
            response.add(
                GetDirThumbUrl2.builder()
                    .pk(file.getId())
                    .thumbUrl2(file.getPhotoDto().getThumbUrl2())
                    .photoId(file.getPhotoDto().getId())
                    .build());
        }
        log.info("service response = {}", response);
        return response;
    }

    public Integer getFileNum(Directory directory) {
        // 초기화
        deque.addFirst(directory);
        visitedSet.add(directory.getId());

        int fileNum = 0;
        while (!deque.isEmpty()) {
            // pop + 해당 폴더의 파일 수 카운트
            Directory curDirectory = deque.pop();
            fileNum += curDirectory.getFileIdList().size();
            // 탐색
            List<String> childDirIdList = curDirectory.getChildDirIdList();
            if (childDirIdList.isEmpty()) {
                continue;
            }
            for (Directory childDirectory : directoryRepository.findAllById(childDirIdList)) {
                // 유효성 검사
                if (visitedSet.contains(childDirectory.getId())) {
                    continue;
                }
                // 인큐
                deque.addFirst(childDirectory);
                // 방첵
                visitedSet.add(childDirectory.getId());
            }
        }
        visitedSet.clear();
        return fileNum;
    }

    private Member getMemberByNone() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        return memberRepository.findById(userId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Party getPartyByDirId(String dirId) {
        Directory dir = directoryRepository.findById(dirId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
        String rootDirId = getRootDirId(dir);
        return partyRepository.findPartyByRootDirId(rootDirId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
    }

    public Boolean isValidRole(String dirId, MemberRole ... memberRoles) {
        MemberParty memberParty = memberpartyRepository.findByMemberAndParty(getMemberByNone(),
                getPartyByDirId(dirId))
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_PARTY_NOT_FOUND));

        // 여기서 MemberRole로 복수로 들어왔을때 or로 내가 넣어준 매개변수에 해당하는 memberParty.getMemberRole();이있ㄷ으면 true 아니면 false
        for (MemberRole role : memberRoles) {
            if(memberParty.getMemberRole().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
