package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Directory;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.GetChildDirDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetDirDtov2;
import project.domain.directory.dto.response.GetDirFullStructureDto;
import project.domain.directory.dto.response.GetDirThumbUrl2;
import project.domain.member.entity.MemberRole;
import project.domain.party.entity.Party;

public interface DirectoryService {

    public DirDto initDirPartyTest(Long partyId);

    public List<GetChildDirDto> getChildDir(String dirId);

    List<GetDirFullStructureDto> getDirFullStructure(String dirId);

    public void initDirParty(Party party);

    public String generateId();

    GetDirDto getDir(String dirId);

    GetDirDtov2 getDirv2(String dirId);

    void createDir(CreateDirDto request);

    // 폴더 이동
    void moveDir(String dirId, String toDirId);

    // 폴더 삭제
    void deleteDir(String dirId, String sseKey);

    // 폴더 이름 수정
    void renameDir(String dirId, String newName);

    // 디렉토리 엔티티 DB에서 조회
    Directory findById(String dirId);

    // 여러가지 있을떄 한방에 저장 할 수 있도록 list 로 묶어준는 메서드
    List<Directory> toList(Directory... directories);

    // 해당 파일의 자식 폴더의 이름 리스트 제공
    List<String> getChildNameList(Directory dir);

    String getParentDirName(Directory directory);

    List<String> getChildDirNameList(Directory directory);

    List<String> getPhotoUrlList(Directory directory);

    List<String> getFullRootByDirId(String dirId);

    List<String> getFullNameByDirId(String dirId);

    String getRootDirId(Directory dir);

    Trash saveFileToTrash(File file, String dirId);

    List<GetDirThumbUrl2> getDirThumbUrl2(String dirId);

    Boolean isValidRole(String dirId, MemberRole ... memberRoles);

}
