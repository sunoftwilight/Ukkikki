package project.domain.photo.service;

import project.domain.photo.dto.response.GroupDetailResDto;
import project.domain.photo.dto.response.GroupbrieflyDto;

import java.util.List;

public interface PhotoGroupService {

    public List<GroupbrieflyDto> getGroups(Long partyId);
    public List<GroupDetailResDto> getGroupDetail(int type, String groupName, Long partyId);

}
