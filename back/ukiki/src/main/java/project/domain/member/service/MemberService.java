package project.domain.member.service;

import project.domain.member.dto.response.KeyGroupDto;

import java.util.List;

public interface MemberService {

    public void setPassword(String password, Long userId);
    public List<KeyGroupDto> getKeyGroup(Long userId, String password);

}
