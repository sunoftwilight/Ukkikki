package project.domain.photo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.File;
import project.domain.directory.repository.FileRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.dto.response.GroupDetailResDto;
import project.domain.photo.dto.response.GroupbrieflyDto;
import project.domain.photo.entity.Face;
import project.domain.photo.entity.FaceGroup;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.FaceGroupRepository;
import project.domain.photo.repository.FaceRepository;
import project.domain.photo.repository.LikesRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoGroupServiceImpl implements PhotoGroupService {

    private final PhotoRepository photoRepository;
    private final PartyRepository partyRepository;
    private final FaceGroupRepository faceGroupRepository;
    private final FaceRepository faceRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final FileRepository fileRepository;


    @Override
    public List<GroupbrieflyDto> getGroups(Long partyId) {
        //파티별로 그룹화된 이름과 썸네잉 url을 반환해야한다
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        //반환할 값
        List<GroupbrieflyDto> groups = new ArrayList<>();
        //메타코드별로 DB 검색
        for (MetaCode metaCode : MetaCode.values()) {
            List<Photo> photoList = photoRepository.findByMetaCode(party, metaCode);
            if (photoList.isEmpty()) {
                continue;
            }
            GroupbrieflyDto group = new GroupbrieflyDto();
            group.setType(1);
            group.setGroupName(metaCode.name());
            group.setThumbnailUrl(photoList.getFirst().getPhotoUrl().getThumb_url1());
            groups.add(group);
        }

        //FACE Group 검색
        List<FaceGroup> faceGroupList = faceGroupRepository.findByPartyId(partyId);
        for (FaceGroup faceGroup : faceGroupList) {
            String faceList = faceGroup.getFaceList();
            if (faceList.equals("[]")){
                continue;
            }
            List<Integer> faces = stringToListFormatter(faceGroup.getFaceList());
            Face face = faceRepository.findById(Long.valueOf(faces.getFirst()))
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
            GroupbrieflyDto group = new GroupbrieflyDto();

            group.setType(2);
            group.setGroupName(String.valueOf(faceGroup.getFaceGroupNumber()));
            group.setThumbnailUrl(face.getFaceImageUrl());
            groups.add(group);
        }

        //좋아요한 사진
        List<Photo> likePhotoList = photoRepository.findByLike(party, member);
        if (likePhotoList.isEmpty()) {
            return groups;
        }
        GroupbrieflyDto group = new GroupbrieflyDto();
        group.setType(3);
        group.setGroupName("좋아요");
        group.setThumbnailUrl(likePhotoList.getFirst().getPhotoUrl().getThumb_url1());
        groups.add(group);

        return groups;
    }

    @Override
    public List<GroupDetailResDto> getGroupDetail(int type, String groupName, Long partyId) {
        //metaCode 그룹인 경우
        List<GroupDetailResDto> groups = new ArrayList<>();
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        if (type == 1) {
            MetaCode metaCode = MetaCode.valueOf(groupName);
            List<Photo> photoList = photoRepository.findByMetaCode(party, metaCode);

            for (Photo photo : photoList) {
                File photoFile = fileRepository.findByPhotoDtoId(photo.getId())
                    .orElse(null);
                GroupDetailResDto groupDetail = new GroupDetailResDto();
                groupDetail.setPhotoId(photo.getId());
                groupDetail.setFileId(photoFile.getId());
                groupDetail.setPhotoUrl(photo.getPhotoUrl().getPhotoUrl());
                groupDetail.setThumbnailUrl(photo.getPhotoUrl().getThumb_url1());
                groups.add(groupDetail);
            }
        }
        //face 그룹인 경우
        if (type == 2) {
            FaceGroup faceGroup = faceGroupRepository.findByPartyIdAndFaceGroupNumber(party.getId(), Integer.parseInt(groupName))
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FACE_GROUP_NOT_FOUND));
            String faceList = faceGroup.getFaceList();
            if (faceList.equals("[]")){
                return groups;
            }
            List<Integer> faces = stringToListFormatter(faceGroup.getFaceList());
            for (Integer faceId : faces) {
                Face face = faceRepository.findById(Long.valueOf(faceId))
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
                GroupDetailResDto groupDetail = new GroupDetailResDto();
                Photo photo = photoRepository.findById(face.getPhotoId())
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
                File photoFile = fileRepository.findByPhotoDtoId(photo.getId())
                    .orElse(null);
                groupDetail.setFileId(photoFile.getId());
                groupDetail.setPhotoId(photo.getId());
                groupDetail.setPhotoUrl(photo.getPhotoUrl().getPhotoUrl());
                groupDetail.setThumbnailUrl(photo.getPhotoUrl().getThumb_url1());
                groups.add(groupDetail);
            }
        }
        // 좋아요한 사진인 경우
        if (type == 3) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = userDetails.getId();

            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

            List<Photo> likePhotoList = photoRepository.findByLike(party, member);
            for (Photo photo : likePhotoList) {
                GroupDetailResDto groupDetail = new GroupDetailResDto();
                File photoFile = fileRepository.findByPhotoDtoId(photo.getId())
                    .orElse(null);
                groupDetail.setFileId(photoFile.getId());
                groupDetail.setPhotoId(photo.getId());
                groupDetail.setPhotoUrl(photo.getPhotoUrl().getPhotoUrl());
                groupDetail.setThumbnailUrl(photo.getPhotoUrl().getThumb_url1());
                groups.add(groupDetail);
            }
        }

        return groups;
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
}
