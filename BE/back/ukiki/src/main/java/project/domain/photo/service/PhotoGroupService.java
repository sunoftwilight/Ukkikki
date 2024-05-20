package project.domain.photo.service;

import project.domain.directory.dto.response.GetDirInnerDtov2;
import project.domain.photo.dto.response.GroupDetailResDto;
import project.domain.photo.dto.response.GroupbrieflyDto;

import java.util.List;

public interface PhotoGroupService {

    public List<GroupbrieflyDto> getGroups(Long partyId);
    public List<GetDirInnerDtov2> getGroupDetail(int type, String groupName, Long partyId);

}
