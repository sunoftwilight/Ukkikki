package project.domain.directory.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import project.domain.directory.dto.PhotoDto;
import project.domain.directory.dto.TrashFileDto;
import project.domain.directory.dto.response.GetDetailFileDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.repository.DirectoryRepository;
import project.domain.directory.repository.FileRepository;
import project.domain.directory.repository.TrashRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.Party;
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
public class FileServiceImpl implements FileService{

    private final S3Util s3Util;

    private final DirectoryService directoryService;
    private final TrashBinService trashBinService;

    private final PartyRepository partyRepository;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final PhotoRepository photoRepository;
    private final TrashRepository trashRepository;
    private final DownloadLogRepository downloadLogRepository;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final FaceRepository faceRepository;



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
    @Transactional
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
    public void deleteOneFile(String fileId, String dirId, String sseKey) {
        // 쓰레기에 file 등록
        File file = findById(fileId);
        Trash fileTrash = saveFileToTrash(file, dirId);
        // 휴지통에 file 등록
        trashBinService.saveFileToTrashBin(fileTrash);
        // 폴더에서 제거
        deleteDirFileRelation(dirId, fileId);
        // 마지막 file 이었다면
        if(file.getDirIdList().isEmpty()){
            // === s3 로직 시작
            // s3 만료일 설정
            Long photoId = file.getPhotoDto().getId();
            Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

            // 인물 분류 사진 만료일 설정
            List<Face> faceList = faceRepository.findByOriginImageUrl(photo.getPhotoUrl().getPhotoUrl());
            for (Face face : faceList) {
                String url = face.getFaceImageUrl();
                String fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".") - 1);
                s3Util.fileExpire(sseKey, fileName);
            }
            // 썸네일 사진 만료일 설정
            for(String url : photo.getPhotoUrl().photoUrls()){
                String fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".") - 1);
                s3Util.fileExpire(sseKey, fileName);
            }
            // 원본 사진 만료일 설정
            s3Util.fileExpire(sseKey, photo.getFileName());
            fileRepository.delete(file);
            // === s3 로직 종료
            fileRepository.deleteById(file.getId());
        }
    }

    @Override
    @Transactional
    public void deleteFileList(List<String> fileIdList, String dirId, String sseKey) {
        if(fileIdList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.EMPTY_FILE_ID_LIST);
        }
        for (String fileId : fileIdList) {
            deleteOneFile(fileId, dirId, sseKey);
        }
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
    public GetDetailFileDto getFile(String fileId) {
        // member 찾기(다운 여부, 좋아요 여부 찾을때 사용)
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        Photo photo = photoRepository.findById(file.getPhotoDto().getId())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

        Optional<DownloadLog> opDownloadLog = downloadLogRepository.findByMemberAndPhoto(
            member, photo);
        Optional<Likes> opLikes = likesRepository.findByMemberAndPhoto(member,
            photo);

        return GetDetailFileDto.builder()
                .url(file.getPhotoDto().getPhotoUrl())
                .isDownload(opDownloadLog.isPresent())
                .isLikes(opLikes.isPresent())
                .build();

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
            .fullRout(directoryService.getFullRootByDirId(dirId))
            .fullName(directoryService.getFullNameByDirId(dirId))
            .build());
    }
}
