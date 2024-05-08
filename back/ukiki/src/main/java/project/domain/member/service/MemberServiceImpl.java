package project.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.domain.member.dto.response.KeyGroupDto;
import project.domain.member.entity.KeyGroup;
import project.domain.member.entity.Member;
import project.domain.member.mapper.KeyGroupMapper;
import project.domain.member.repository.KeyGroupRepository;
import project.domain.member.repository.MemberRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.BcryptUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final KeyGroupRepository keyGroupRepository;
    private final KeyGroupMapper keyGroupMapper;
    private final BcryptUtil bcryptUtil;

    @Override
    public void setPassword(String password, Long userId) {
        //맴버 객체 찾아오기
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        //멤버 password 컬럼에 값추가
        member.setPassword(bcryptUtil.encodeBcrypt(password));
        memberRepository.save(member);
    }

    @Override
    public List<KeyGroupDto> getKeyGroup(Long userId, String password) {
        //userId로 유저 검색해서 객체 가져오고
        Member member = memberRepository.findById(userId)
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
