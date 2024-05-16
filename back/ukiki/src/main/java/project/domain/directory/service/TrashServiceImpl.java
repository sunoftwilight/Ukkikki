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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.directory.collection.DataType;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashBinRepository;
import project.domain.directory.repository.TrashRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.MemberRole;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.photo.entity.Face;
import project.domain.photo.entity.FaceGroup;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.FaceGroupRepository;
import project.domain.photo.repository.FaceRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.S3Util;

@Service
@AllArgsConstructor
@Slf4j
public class TrashServiceImpl implements TrashService{


    private static ModelMapper modelMapper = new ModelMapper();
    private final S3Util s3Util;

    private TrashBinService trashBinService;
    private final DirectoryRepository directoryRepository;
    private final TrashBinRepository trashBinRepository;
    private final TrashRepository trashRepository;
    private final FileRepository fileRepository;
    private final PhotoRepository photoRepository;
    private final FaceRepository faceRepository;
    private final FaceGroupRepository faceGroupRepository;
    private final MemberpartyRepository memberpartyRepository;
    private final MemberRepository memberRepository;

    @Override
    public void getTrash() {

    }

    @Override
    // trashId != rawId
    @Transactional
    public void restoreOneTrash(String trashId, Long trashBinId, String sseKey) {
        TrashBin trashBin = trashBinService.findById(trashBinId);
        Trash trash = trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));
        // 유효성 검사
        if (isOutOfRecoveryPeriod(trash)) {
            throw new BusinessLogicException(ErrorCode.DIRECTORY_OUT_OF_DEADLINE);
        }

        // 폴더인지 파일인지 분기 처리
        if (trash.getDataType() == DataType.DIRECTORY) {
            log.info("DataType = DIRECTORY");
//            List<Trash> allTrash = getAllTrash(trash.getId());
            List<Trash> allTrash = getAllTrashV2(trash.getId());
            log.info("모든 쓰레기 = {}", allTrash);
            // 일단 가장 첫번째인 폴더는 휴지통에서 제거 + 본 디렉토리에서 관계 회복
            Directory trashDirDto = modelMapper.map(trash.getContent(), Directory.class);
            setConnection(trash);

            for (Trash oneTrash : allTrash) {
                log.info("oneTrash = {}", oneTrash);
                if (oneTrash.getDataType() == DataType.DIRECTORY) {
                    log.info("디렉토리타입 쓰레기 복원 작업");
                    Directory oneTrashDirDto = modelMapper.map(oneTrash.getContent(), Directory.class);
                    // 폴더 복원
                    setDir(oneTrash, oneTrashDirDto);
                    // 휴지통에서 제거
                    trashRepository.delete(oneTrash);

                } else if (oneTrash.getDataType() == DataType.FILE) {
                    log.info("파일타입 쓰레기 복원 작업 시작");
                    // content 가져오기
                    TrashFileDto oneTrashFileDto = modelMapper.map(oneTrash.getContent(),
                        TrashFileDto.class);
                    // file 복원
                    setFile(oneTrash, oneTrashFileDto, sseKey);
                    // 쓰레기 지우기
                    trashRepository.delete(oneTrash);
                }
            }
            // 휴지통에서 제거
            trashBin.getDirTrashIdList().remove(trash.getId());
            trashBinRepository.save(trashBin);
            return;

        } else if (trash.getDataType() == DataType.FILE) {
            log.info("DataType = FILE");
            // content 가져오기
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
            // dir 관계 복구
            setConnection(trash);
            // file 복구
            setFile(trash, trashFileDto, sseKey);
            // trashBin 제거
            deleteFileFromTrashBin(trash.getId(), trashBinId);
            // trash 제거
            trashRepository.delete(trash);
            return;
        }
        throw new BusinessLogicException(ErrorCode.TRASH_CLASSIFICATION_FAIL);
    }

    @Override
    @Transactional
    public void restoreTrashList(List<String> trashIdList, Long trashBinId, String sseKey) {
        if (!isValidRole(trashBinId, MemberRole.EDITOR, MemberRole.MASTER)) {
            throw new BusinessLogicException(ErrorCode.INVALID_MEMBER_ROLE);
        }

        log.info("come in restoreTrashList service");
        for (String trashId : trashIdList) {
            restoreOneTrash(trashId, trashBinId, sseKey);
        }
        log.info("response restoreTrashList = void");
    }

    @Override
    public List<Trash> getAllTrash(String trashIdDirType) {
        long startTime = System.nanoTime();
        List<Trash> result = new ArrayList<>();

        // 초기화
        Deque<Trash> deque = new ArrayDeque<>();
        HashSet<String> visitedSet = new HashSet<>();
        Trash trash = findById(trashIdDirType);
        deque.addFirst(trash);
        visitedSet.add(trashIdDirType);
        result.add(trash);

        while (!deque.isEmpty()) {
            // 디큐, result에 저장, 모든 file의 TrashId 저장
            Trash curTrash = deque.pollLast();
            // trashDirType 저장
            Directory curDirInTrash = modelMapper.map(curTrash.getContent(), Directory.class);
            List<String> fileRawIdList = curDirInTrash.getFileIdList();

            if (!fileRawIdList.isEmpty()) {
                for (String fileRawId : fileRawIdList) {
                    List<Trash> trashFileTypeList = trashRepository.findByRawId(fileRawId);
                    log.info("List<Trash> = {}", trashFileTypeList);
                    if(trashFileTypeList.isEmpty()) {
                        throw new BusinessLogicException(ErrorCode.FILE_NOT_FOUND);
                    }
                    Trash trashFileType = null;
                    for (Trash fileTrash : trashFileTypeList) {
                        TrashFileDto trashFileDto = modelMapper.map(fileTrash.getContent(),
                            TrashFileDto.class);
                        if (trashFileDto.getDirId().equals(curDirInTrash.getId())) {
                            trashFileType = fileTrash;
                            break;
                        }
                    }
                    if (trashFileType == null) {
                        throw new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND);
                    }
                    log.info("검색된 TrashFileType = {}", trashFileType);
                    result.add(trashFileType);
                }
            }
            // 탐색
            // trashToDir 저장
            List<String> childDirRawIdList = curDirInTrash.getChildDirIdList();
            if (!childDirRawIdList.isEmpty()) {
                for (String dirRawId : childDirRawIdList) {
                    if(visitedSet.contains(dirRawId)){
                        continue;
                    }
                    Trash trashDirType = trashRepository.findFirstByRawId(dirRawId)
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
                    deque.addFirst(trashDirType);
                    visitedSet.add(dirRawId);
                    result.add(trashDirType);
                }
            }
        }
        // 다음 요청을 위해 방문체크 지워주기
        visitedSet.clear();

        long endTime = System.nanoTime();  // 종료 시간 측정
        long duration = (endTime - startTime);  // 실행 시간 계산 (나노초 단위)
        // 로깅
        log.info("getAllTrash duration time: {} nanoseconds", duration);
        return result;
    }

    @Override
    public List<Trash> getAllTrashV2(String trashIdDirType) {
        long startTime = System.nanoTime();

        List<Trash> result = new ArrayList<>();
        Map<String, Trash> cache = new HashMap<>();

        Deque<Trash> deque = new ArrayDeque<>();
        HashSet<String> visitedSet = new HashSet<>();
        Trash initialTrash = findById(trashIdDirType);
        deque.add(initialTrash);
        visitedSet.add(trashIdDirType);
        result.add(initialTrash);

        while (!deque.isEmpty()) {
            Trash currentTrash = deque.poll();
            Directory directoryInTrash = modelMapper.map(currentTrash.getContent(),
                Directory.class);

            // 파일 처리
            processFiles(directoryInTrash, result, cache);

            // 디렉토리 처리
            processDirectories(directoryInTrash, deque, visitedSet, result, cache);
        }

        long endTime = System.nanoTime();  // 종료 시간 측정
        long duration = (endTime - startTime);  // 실행 시간 계산 (나노초 단위)
        // 로깅
        log.info("getAllTrashV2 duration time: {} nanoseconds", duration);
        return result;
    }

    private void processFiles(Directory directory, List<Trash> result, Map<String, Trash> cache) {
        for (String fileId : directory.getFileIdList()) {
            if (cache.containsKey(fileId)) {
                result.add(cache.get(fileId));
                continue;
            }
            List<Trash> trashes = trashRepository.findByRawId(fileId);
            for (Trash trash : trashes) {
                TrashFileDto dto = modelMapper.map(trash.getContent(), TrashFileDto.class);
                if (dto.getDirId().equals(directory.getId())) {
                    cache.put(fileId, trash);
                    result.add(trash);
                    break;
                }
            }
        }
    }

    private void processDirectories(Directory directory, Deque<Trash> deque,
        HashSet<String> visitedSet, List<Trash> result, Map<String, Trash> cache) {
        for (String dirId : directory.getChildDirIdList()) {
            if (!visitedSet.contains(dirId)) {
                visitedSet.add(dirId);
                if (cache.containsKey(dirId)) {
                    Trash cachedTrash = cache.get(dirId);
                    deque.addFirst(cachedTrash);
                    result.add(cachedTrash);
                    continue;
                }
                Trash newTrash = trashRepository.findFirstByRawId(dirId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
                cache.put(dirId, newTrash);
                deque.addFirst(newTrash);
                result.add(newTrash);
            }
        }
    }

    @Override
    public void deleteOneTrash(String trashId, Long trashBinId) {
        log.info("come in deleteOneTrash");
        log.info("trashId = {}", trashId);
        log.info("trashBinId = {}", trashBinId);
        Trash trash = findById(trashId);
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));

        if (trash.getDataType() == DataType.FILE) {
            TrashFileDto trashFileDto = modelMapper.map(trash.getContent(), TrashFileDto.class);
            Long photoId = trashFileDto.getPhotoDto().getId();

            // 포토 관련 설정과 S3에서 지우기 설정은 여기에서
            setPhotoAndS3(photoId, trashBinId);

            // 쓰레기에서 trash 지우기
            trashRepository.deleteById(trash.getId());
            // 쓰레기통에서 trashId 지우기
            trashBin.getFileTrashIdList().remove(trash.getId());
            trashBinRepository.save(trashBin);
        } else if (trash.getDataType() == DataType.DIRECTORY) {
            // 쓰레기가 폴더인 경우
            // trashBin에서 trashId 제거
            trashBin.getDirTrashIdList().remove(trashId);
            trashBinRepository.save(trashBin);
            // 해당 쓰레기 디렉토리 하위 모든 쓰레기 검색(본인 포함)
//            List<Trash> allTrash = getAllTrash(trashId);
            List<Trash> allTrash = getAllTrashV2(trashId);
            for (Trash oneTrash : allTrash) {
                log.info("oneTrashId = {}", oneTrash.getId());
                if (oneTrash.getDataType() == DataType.DIRECTORY) {
                    trashRepository.delete(oneTrash);
                } else if (oneTrash.getDataType() == DataType.FILE) {
                    TrashFileDto oneTrashFileDto = modelMapper.map(oneTrash.getContent(), TrashFileDto.class);
                    log.info("trashFileDto = {}", oneTrashFileDto);
                    log.info("photoDto = {}", oneTrashFileDto.getPhotoDto());
                    log.info("photoId = {}", oneTrashFileDto.getPhotoDto().getId());

                    Long photoId = oneTrashFileDto.getPhotoDto().getId();

                    // 포토 관련 설정과 S3에서 지우기 설정은 여기에서
                    setPhotoAndS3(photoId, trashBinId);

                    // 쓰레기에서 trash 지우기
                    trashRepository.deleteById(oneTrash.getId());
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteTrashList(Long trashBinId, List<String> trashIdList) {
        log.info("come in deleteTrashList service");
        if (!isValidRole(trashBinId, MemberRole.MASTER)) {
            throw new BusinessLogicException(ErrorCode.INVALID_MEMBER_ROLE);
        }
        log.info("pass validation");

        for (String trashId : trashIdList) {
            deleteOneTrash(trashId, trashBinId);
        }
        log.info("response deleteTrashList = void");
    }


    public void setPhotoAndS3(Long photoId, Long trashBinId) {
        // 포토 수 - 1감소
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

        int photoNum = photo.getPhotoNum();
        photoNum = photoNum - 1;
        log.info("갱신된 photoNum = {}", photoNum);

        if (photoNum != 0) {
            log.info("photoNum 갱신 로직 실행");
            // 그냥 photoNum 갱신하고 return
            photo.setPhotoNum(photoNum);
            photoRepository.save(photo);
            return;
        }
        log.info("photo, s3 삭제 로직 실행");
        // 0일경우 S3에서 관련 url 삭제와 photo에서 삭제
        // == S3로직 시작
        // photo와 연관되어 있는 얼굴 분류 사진 삭제
        List<Face> faceList = faceRepository.findByOriginImageUrl(photo.getPhotoUrl().getPhotoUrl());
        for (Face face : faceList) {
            String url = face.getFaceImageUrl();
            String fileName = url.split("/")[3];
            //S3 파일 삭제
            s3Util.fileDelete(fileName);
            //FaceGroup DB 수정
            FaceGroup faceGroup = faceGroupRepository.findByPartyIdAndFaceGroupNumber(trashBinId , face.getFaceGroupNumber())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FACE_GROUP_NOT_FOUND));
            List<Integer> faces = stringToListFormatter(faceGroup.getFaceList());
            //FaceGroup Entity faceList 에서 해당 faceId 삭제
            faces.remove((Long)face.getFaceId());
            //Entity update
            faceGroup.setFaceList(faces.toString());
            //리스트가 비었다면 group 삭제
            if(faces.isEmpty()) {
                faceGroupRepository.delete(faceGroup);
            }else{
                faceGroupRepository.save(faceGroup);
            }
            // DB 에서 해당 face 삭제
            faceRepository.delete(face);
        }
        // 썸네일 사진 삭제
        for(String url : photo.getPhotoUrl().photoUrls()){
            String fileName = url.split("/")[3];
            s3Util.fileDelete(fileName);
        }
        // 원본 사진 삭제
        s3Util.fileDelete(photo.getFileName());
        //== s3 로직 종료
        // photo 처리 부분
        photoRepository.delete(photo);
    }

    public List<Integer> stringToListFormatter(String input) {
        // 괄호 제거
        String trimmed = input.substring(1, input.length() - 1);

        // 쉼표로 분리하고 각 요소를 정수로 변환하여 리스트 생성
        List<Integer> numbers = Arrays.stream(trimmed.split(","))
            .map(String::trim) // 공백 제거
            .map(Integer::parseInt) // 정수로 변환
            .collect(Collectors.toList());

        // 결과 출력
        log.info("numbers: {}", numbers);
        return numbers;
    }

    @Override
    public Trash findById(String trashId) {
        return trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASH_NOT_FOUND));
    }

    @Override
    public Boolean isOutOfRecoveryPeriod(Trash deletedDate) {
        LocalDate deadLine = deletedDate.getDeadLine();
        LocalDate now = LocalDate.now();
        return deadLine.isBefore(now);
    }

    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID()).append(LocalDateTime.now());
        return String.valueOf(sb);
    }

    @Override
    public void deleteDirFromTrashBin(String trashId, Long trashBinId) {
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getDirTrashIdList().remove(trashId);
        trashBinRepository.save(trashBin);
    }

    @Override
    public void deleteFileFromTrashBin(String trashId, Long trashBinId) {
        TrashBin trashBin = trashBinRepository.findById(trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.TRASHBIN_NOT_FOUND));
        trashBin.getFileTrashIdList().remove(trashId);
        trashBinRepository.save(trashBin);
    }

    // dir 생성
    @Override
    public void setDir(Trash trash, Directory trashDirDto) {
        log.info("come in setDir");
        Optional<Directory> opDir = directoryRepository.findById(trash.getRawId());
        // 폴더에 없는 경우 쓰레기 정보를 바탕으로 새로 만들어 저장
        if(opDir.isEmpty()) {
            directoryRepository.save(Directory.builder()
                .id(trash.getRawId())
                .dirName(trashDirDto.getDirName())
                .parentDirId(trashDirDto.getParentDirId())
                .childDirIdList(trashDirDto.getChildDirIdList())
                .fileIdList(trashDirDto.getFileIdList())
                .build());
        } else {
        // 복구하려는 폴더가 이미 공유 앨범에 생성 된 상태일때 쓰레기 정보를 추가
            Directory directory = opDir.get();
            List<String> childDirIdList = directory.getChildDirIdList();
            List<String> fileIdList = directory.getFileIdList();

            // childDirIdList 정보 추가
            for(String childDirId : trashDirDto.getChildDirIdList()) {
                if(childDirIdList.contains(childDirId)){continue;}
                childDirIdList.add(childDirId);
            }
            // fileIdList 정보 추가
            fileIdList.addAll(trashDirDto.getFileIdList());
            // 저장
            directoryRepository.save(directory);
        }
    }

    // file 생성
    @Override
    public void setFile(Trash trash, TrashFileDto trashFileDto, String sseKey) {
        log.info("come in setFile");
        Optional<File> opFile = fileRepository.findById(trash.getRawId());
        // 폴더에 이미 file이 있는경우 그놈한테 dirId 등록
        if (opFile.isPresent()) {
            File file = opFile.get();
            file.getDirIdList().add(trashFileDto.getDirId());
            fileRepository.save(file);
            return;
        }
        // 없다면 새로 file을 만들어 주기
        fileRepository.save(
            File.builder()
                .id(trash.getRawId())
                .photoDto(trashFileDto.getPhotoDto())
                .dirIdList(Arrays.asList(trashFileDto.getDirId()))
                .build()
        );

        // ===s3 시작
        // 만약 새로 생성되는 file의 매핑되는 photo의 num = 1 인경우 undo
        Photo photo = photoRepository.findById(trashFileDto.getPhotoDto().getId())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));
        if(photo.getPhotoNum() == 1) {
            // undo 실행
            List<Face> faceList = faceRepository.findByOriginImageUrl(photo.getPhotoUrl().getPhotoUrl());
            // 얼굴 분류 사진 만료 설정 취소
            for (Face face : faceList) {
                String url = face.getFaceImageUrl();
                String fileName = url.split("/")[3];
                s3Util.fileUndo(sseKey, fileName);
            }
            // 썸네일 사진 만료일 설정 취소
            for(String url : photo.getPhotoUrl().photoUrls()){
                String fileName = url.split("/")[3];
                s3Util.fileUndo(sseKey, fileName);
            }
            // 원본 사진 만료일 설정 취소
            s3Util.fileUndo(sseKey, photo.getFileName());
        }
        // ===s3 종료
    }


    // trash 복구를 위한 빈 폴더 생성과 endPoint 연결 사전 작업
    @Transactional
    public void setConnection(Trash trash) {
        log.info("come in setConnection");
        List<String> fullRoutList = trash.getFullRout();
        List<String> fullNameList = trash.getFullName();
        int sizeOfFullRoutList = fullRoutList.size();
        log.info("sizeOfFullRoutList = {}", sizeOfFullRoutList);
        // root에서 지워진 경우
        if(sizeOfFullRoutList == 1) {
            // 끝맺고 마무리
            setEndPoint(trash, fullRoutList, sizeOfFullRoutList);
            return;
        }

        // 깡통 폴더 생성
        for(int i = 1; i < sizeOfFullRoutList; i++) {
            Optional<Directory> opDir = directoryRepository.findById(fullRoutList.get(i));
            log.info("{} 번째 시도", i - 1);
            if(opDir.isPresent()) {log.info("id : {}에 해당하는 dir 존재", fullRoutList.get(i));continue;}
            //-- i에 해당하는 폴더가 없는경우 => 새로 만들어 줘야됨
            log.info("존재하지 않습니다. 새로 만듭니다.");
            Directory parentDir = directoryRepository.findById(fullRoutList.get(i - 1))
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.DIRECTORY_NOE_FOUND));
            log.info("parentDirId = {}",parentDir.getId());
            // 부모에 자식 dirId 등록
            parentDir.getChildDirIdList().add(fullRoutList.get(i));
            directoryRepository.save(parentDir);
            // 자식 dir 생성
            directoryRepository.save(
                Directory.builder()
                    .id(fullRoutList.get(i))
                    .dirName(fullNameList.get(i))
                    .parentDirId(fullRoutList.get(i - 1))
                    .build()
            );
        }

        setEndPoint(trash, fullRoutList, sizeOfFullRoutList);
    }

    public void setEndPoint(Trash trash, List<String> fullRoutList, int sizeOfFullRoutList) {
        // endPoint에 fileId or dirId 부여하기
        Directory directory = directoryRepository.findById(
                fullRoutList.get(sizeOfFullRoutList - 1))
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        switch (trash.getDataType()) {
            case FILE:
                directory.getFileIdList().add(trash.getRawId());
                break;
            case DIRECTORY:
                directory.getChildDirIdList().add(trash.getRawId());
                break;
            default:
                throw new BusinessLogicException(ErrorCode.TRASH_CLASSIFICATION_FAIL);
        }
        directoryRepository.save(directory);
    }

    public Boolean isValidRole(Long trashBinId, MemberRole... memberRoles) {
        MemberParty memberParty = memberpartyRepository.findByMemberIdAndPartyId(getMemberIdByNone(), trashBinId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_PARTY_NOT_FOUND));

        // 여기서 MemberRole로 복수로 들어왔을때 or로 내가 넣어준 매개변수에 해당하는 memberParty.getMemberRole();이있ㄷ으면 true 아니면 false
        for (MemberRole role : memberRoles) {
            if(memberParty.getMemberRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    private Long getMemberIdByNone() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        return userDetails.getId();
    }

}
