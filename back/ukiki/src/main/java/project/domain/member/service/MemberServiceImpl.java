package project.domain.member.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.dto.response.InfoDto;
import project.domain.member.dto.response.KeyGroupDto;
import project.domain.member.entity.KeyGroup;
import project.domain.member.entity.Member;
import project.domain.member.mapper.KeyGroupMapper;
import project.domain.member.redis.MemberToken;
import project.domain.member.repository.KeyGroupRepository;
import project.domain.member.repository.MemberRepository;
import project.domain.member.repository.MemberTokenRedisRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.jwt.JWTUtil;
import project.global.util.BcryptUtil;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final MemberTokenRedisRepository memberTokenRedisRepository;
    private final JWTUtil jwtUtil;
    private final KeyGroupRepository keyGroupRepository;
    private final KeyGroupMapper keyGroupMapper;
    private final BcryptUtil bcryptUtil;
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
                infoDto.setUploadGroupId(member.getUploadGroupId());
                infoDto.setInsertPass(member.getPassword() != null);
            } else{
                infoDto = new InfoDto();
                infoDto.setUserId(userId);
                infoDto.setUserName(userDetails.getUsername());
                infoDto.setProfileUrl(null);
                infoDto.setUploadGroupId(null);
                infoDto.setInsertPass(false);
            }
        }

        return infoDto;
    }

    @Override
    public String reissue(Cookie[] cookies) {

        String refresh = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refresh")){
                    refresh = cookie.getValue();
                }
            }

        }

        // 토큰이 없어요.
        if(refresh == null){
            throw new BusinessLogicException(ErrorCode.REFRESH_TOKEN_NULL);
        }

        // refresh토큰 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new BusinessLogicException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }


        String reUsername = jwtUtil.getUsername(refresh);
        String reProviderId = jwtUtil.getProviderId(refresh);
        Long id = jwtUtil.getId(refresh);

        MemberToken memberToken = memberTokenRedisRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.REFRESH_TOKEN_NULL));

        // 토큰이 일치하지 않아요.
        if(!refresh.equals(memberToken.getToken())){
            throw new BusinessLogicException(ErrorCode.REFRESH_TOKEN_MATCH);
        }

//        refresh = jwtUtil.createJWT("access", id, reUsername, reProviderId, ((1000L * 60) * 60 * 4));

        return jwtUtil.createJWT("access", id, reUsername, reProviderId, ((1000L * 60) * 60 * 4));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        String authorization = request.getHeader("authorization");
        String access = null;

        if(authorization != null){
            access = authorization.split(" ")[1];
        }

        String refresh = null;

        // refresh 검색
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
                break;
            }
        }

        if(refresh == null){
            throw new BusinessLogicException(ErrorCode.REFRESH_TOKEN_NULL);
        }

        Long accesId = jwtUtil.getId(access);
        Long refreshId = jwtUtil.getId(refresh);

        if(!accesId.equals(refreshId)){
            throw new BusinessLogicException(ErrorCode.MEMBER_LOGOUT_MATCH);
        }

        // redis 삭제
        memberTokenRedisRepository.findById(accesId).ifPresent(memberToken -> memberTokenRedisRepository.deleteById(accesId));

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        cookie = new Cookie("isLogin", "false");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void setPassword(String password) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //맴버 객체 찾아오기
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        //멤버 password 컬럼에 값추가
        member.setPassword(bcryptUtil.encodeBcrypt(password));
        memberRepository.save(member);

    }

    @Override
    public List<KeyGroupDto> getKeyGroup(String password) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //userId로 유저 검색해서 객체 가져오고
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        //password 맞는지 검증하고
        if(!bcryptUtil.matchesBcrypt(password, member.getPassword())){
            throw new BusinessLogicException(ErrorCode.USER_PASSWORD_INVALID);
        }
        //keyGroup 정보 가져다가 반환
        List<KeyGroup> keyGroupList = keyGroupRepository.findByMember(member);

        return keyGroupMapper.toKeyGroupDtoList(keyGroupList);
    }

}
