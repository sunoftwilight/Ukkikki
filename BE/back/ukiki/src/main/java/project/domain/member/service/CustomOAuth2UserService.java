package project.domain.member.service;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.dto.request.KakaoResponse;
import project.domain.member.dto.request.OAuth2Response;
import project.domain.member.dto.response.MemberDto;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;


@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 요청 들어온 id가 kakao인가 naver인지 확인하기 위한 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        // 카카오 id를 가져온 후
        Member member = memberRepository.findByProviderId(oAuth2Response.getProviderId());
        // 저장.

        MemberDto memberDto = saveUser(member, oAuth2Response);

        return new CustomOAuth2User(memberDto);
    }

    // 회원 정보 저장
    public MemberDto saveUser(Member member, OAuth2Response oAuth2Response){
        // 회원 정보가 없을 시.
        if(member == null){
            member = new Member();
            member.setProviderId(oAuth2Response.getProviderId());
            member.setUserName(oAuth2Response.getUserName());
            member.setProfileUrl(oAuth2Response.getProfileUrl());
        }
        // 기존 회원.
        else{
            member.setUserName(oAuth2Response.getUserName());
            member.setProfileUrl(oAuth2Response.getProfileUrl());
        }

        memberRepository.save(member);


        // Dto 반환
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setUserName(member.getUserName());
        memberDto.setProviderId(member.getProviderId());

        return memberDto;
    }
}
