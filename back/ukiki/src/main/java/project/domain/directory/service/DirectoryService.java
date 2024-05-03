package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.RenameDirDto;
import project.domain.party.entity.Party;

public interface DirectoryService {

    /**
     * 파티 생성시 방 생성 로직입니다. party의 속성에 추가해야함니다.
     * 초기 생성 폴더의 parent_id = null 입니다.
     *
     * @param partyId : 파티 식별자
     * @return : 폴더정보 DTO
     */
    public DirDto initDirPartyTest(Long partyId);

    public void initDirParty(Party party);
//    public InitDirDto initDirMember(Long memberId);

    /**
     * id 생성을 위한 로직입니다.
     *
     * @return UUID + LocalDateTime
     */
    public String generateId();

    /**
     * 폴더 조회 로직입니다.
     *
     * @param dirId
     * @return : 조회하려는 폴더의 정보 전부
     */
    public GetDirDto getDir(String dirId);
    public GetDirDto createDir(CreateDirDto request);
    // 폴더 이동
    public GetDirDto moveDir(String dirId, String toDirId);
    // 폴더 삭제
    public GetDirDto deleteDir(String dirId);
    // 폴더 이름 수정
    public RenameDirDto renameDir(String dirId, String newName);
    // 디렉토리 엔티티 DB에서 조회
    public Directory findById(String dirId);
    // 여러가지 있을떄 한방에 저장 할 수 있도록 list 로 묶어준는 메서드
    public List<Directory> toList(Directory... directories);
    // 해당 파일의 자식 폴더의 이름 리스트 제공
    public List<String> getChildNameList(Directory dir);
    String getParentDirName(Directory directory);
    List<String> getChildDirNameList(Directory directory);
    List<String> getPhotoUrlList(Directory directory);

    String getRootDirId(Directory dir);

}
