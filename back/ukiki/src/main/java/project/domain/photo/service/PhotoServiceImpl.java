package project.domain.photo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.File;
import project.domain.directory.repository.FileRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.request.MemoModifyDto;
import project.domain.photo.dto.response.MemoListDto;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.mediatable.Likes;
import project.domain.photo.entity.mediatable.Memo;
import project.domain.photo.repository.LikesRepository;
import project.domain.photo.repository.MemoRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService{

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;
    private final FileRepository fileRepository;
    private final LikesRepository likesRepository;

    @Override
    public List<MemoListDto> memo(String fileId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 유저 확인
        if(userDetails == null){
            throw new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Long memberId = userDetails.getId();

        // GUEST 차단
        if(memberId == 0){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        return memoRepository.findByPhotoId(file.getPhotoDto().getId());
    }

    @Override
    @Transactional
    public void memoCreate(MemoDto memoDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 유저 확인
        if(userDetails == null){
            throw new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Long memberId = userDetails.getId();

        // GUEST 차단
        if(memberId == 0){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        // 유정 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));


        File file = fileRepository.findById(memoDto.getFileId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        // 사진 정보 조회
        Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));


        // 메모 생성
        Memo build = Memo.customBuilder()
                .photo(photo)
                .member(member)
                .content(memoDto.getContent())
                .date(myDate())
                .build();

        // 메모 저장
        memoRepository.save(build);
    }

    @Override
    @Transactional
    public void memoModify(MemoModifyDto memoModifyDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 유저 확인
        if (userDetails == null) {
            throw new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Long memberId = userDetails.getId();

        // GUEST 차단
        if (memberId == 0) {
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        Memo memo = memoRepository.findById(memoModifyDto.getMemoId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMO_NOT_FOUND));

        Long memoMemberId = memo.getMember().getId();

        // 현재 회원과 메모를 등록한 회원이 다르다면.
        if (!memberId.equals(memoMemberId)) {
            throw new BusinessLogicException(ErrorCode.MEMBER_NOT_MATCH);
        }

        memo.setContent(memoModifyDto.getContent());
        memo.setDate(myDate());

        memoRepository.save(memo);
    }

    @Override
    @Transactional
    public void memoDelete(Long memoId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 유저 확인
        if (userDetails == null) {
            throw new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Long memberId = userDetails.getId();

        // GUEST 차단
        if (memberId == 0) {
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMO_NOT_FOUND));

        Long memoMemberId = memo.getMember().getId();

        // 현재 회원과 메모를 등록한 회원이 다르다면.
        if (!memberId.equals(memoMemberId)) {
            throw new BusinessLogicException(ErrorCode.MEMBER_NOT_MATCH);
        }

        memo.delete();
    }

    @Override
    @Transactional
    public void likesCreate(String fileId) {
        log.info("come likesCreate Service");
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        Photo photo = photoRepository.findById(file.getPhotoDto().getId())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

        Likes build = Likes.customBuilder()
            .member(member)
            .photo(photo)
            .build();

        log.info("result service = {}", build);

    }

    @Override
    @Transactional
    public void likesDelete(String fileId) {
        log.info("come likesDelete Service");
        // memberId 찾기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        // photoId 찾기
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        Long photoId = file.getPhotoDto().getId();
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_NOT_FOUND));

        // likes 찾기
        Likes likes = likesRepository.findByMemberAndPhoto(member, photo)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.LIKES_NOT_FOUND));

        // likes 제거하기
        likes.delete();
    }


    public String myDate(){

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }
}
