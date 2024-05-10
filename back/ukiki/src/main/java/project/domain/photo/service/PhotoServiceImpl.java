package project.domain.photo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.directory.collection.File;
import project.domain.directory.repository.FileRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.mediatable.Memo;
import project.domain.photo.repository.MemoRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService{

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;
    private final FileRepository fileRepository;

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
                .build();

        // 메모 저장
        memoRepository.save(build);
    }
}
