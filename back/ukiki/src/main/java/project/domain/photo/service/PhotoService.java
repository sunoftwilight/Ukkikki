package project.domain.photo.service;


import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.request.MemoModifyDto;

public interface PhotoService {
    public void memoCreate(MemoDto memoDto);

    public void memoModify(MemoModifyDto memoModifyDto);

    public void memoDelete(Long memoId);

    public void likesCreate(String fileId);

    public void likesDelete(String fileId);
}
