package project.domain.directory.service;

import java.util.List;
import project.domain.directory.collection.File;
import project.domain.directory.collection.Trash;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetFileDto;
import project.domain.photo.entity.Photo;

public interface FileService {

    // file생성
    void createFile(Long partyId, Photo photo);

    // 사진 복사
    GetDirDto copyFile(String fileId, String fromDirId, String toDirId);

    // 사진 이동
    GetDirDto moveFile(String fileId, String fromDirId, String toDirId);

    // 단일 사진 삭제
    GetDirDto deleteOneFile(String fileId, String dirId);

    // 전체 사진 삭제
    GetDirDto deleteAllFile(String fileId, String dirId);

    // 선택 사진 삭제
    GetDirDto deleteSelectedFile(List<String> fileIdList, String dirId);

    public Trash saveFileToTrash(File file, String dirId);

    // dir, file의 관계 설정 메서드
    void setDirFileRelation(String dirId, String fileId);

    // dir, file의 관계 삭제 메서드
    void deleteDirFileRelation(String dirId, String fileId);

    public String generateId();

    public File findById(String fileId);

    GetFileDto getFileDto(String fileId);
}
