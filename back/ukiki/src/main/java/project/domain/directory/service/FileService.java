package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.File;

public interface FileService {

    // file생성
    void createFile(Long partyId, Long photoId);

    // 사진 복사
    void copyFile(String targetDirId, String fileId);

    // 사진 이동
    void moveFile(String fromDirId, String toDirId, String fileId);

    // 단일 사진 삭제
    void deleteOneFile(String dirId, String fileId);

    // 전체 사진 삭제
    void deleteAllFile(String fileId);

    // 선택 사진 삭제
    void deleteSelectedFile(List<String> fileIdList);

    // dir, file의 관계 설정 메서드
    void setDirFileRelation(String dirId, String fileId);

    // dir, file의 관계 삭제 메서드
    void deleteDirFileRelation(String dirId, String fileId);

    public String generateId();

    public File findById(String fileId);
}
