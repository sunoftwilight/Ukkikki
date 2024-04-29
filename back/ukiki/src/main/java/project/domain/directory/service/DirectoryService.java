package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.request.MoveDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.RenameDirDto;

public interface DirectoryService {

    /**
     * 파티 생성시 방 생성 로직입니다. party의 속성에 추가해야함니다.
     * 초기 생성 폴더의 parent_id = null 입니다.
     *
     * @param partyId : 파티 식별자
     * @return : 폴더정보 DTO
     */
    public DirDto initDirParty(Long partyId);
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
    public DirDto getDir(String dirId);

    public DirDto createDir(CreateDirDto request);

    // 폴더 이동
    public DirDto moveDir(MoveDirDto request);

    // 폴더 삭제
    public DirDto deleteDir(String dirId);

    // 폴더 복구
    public DirDto restoreDir(String dirId);

    // 폴더 이름 수정
    public RenameDirDto renameDir(project.domain.directory.dto.request.RenameDirDto request);

    public Directory findById(String dirId);

    public List<Directory> toList(Directory... directories);
}
