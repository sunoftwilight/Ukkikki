package project.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.dto.response.InfoDto;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    /*
    내 정보를 처음 가져올 때 사용할 함수.
     */
    @Override
    public InfoDto myInfo() {

        // 로그인 성공했을때 저장해두었던 값을들 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        InfoDto infoDto = null;

        // 널 값 체크
        if(authentication != null){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            long userId = userDetails.getId();

            Optional<Member> optionalMember = memberRepository.findById(userId);
            // 널 값 체크
            if(optionalMember.isPresent()){
                // 불러온 값들을 Dto에 넣어 리턴한다.
                Member member = optionalMember.get();

                infoDto = new InfoDto();
                infoDto.setProfileUrl(member.getProfileUrl());
                infoDto.setUserName(member.getUserName());
                infoDto.setUserId(member.getId());

            }
        }

        return infoDto;
    }
}
