package project.domain.photo.service;


import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.request.MemoModifyDto;
import project.domain.photo.dto.response.MemoListDto;
import project.domain.photo.entity.mediatable.Memo;

import java.util.List;

public interface PhotoService {
    public List<MemoListDto> memo(String fileId);
    public void memoCreate(MemoDto memoDto);

    public void memoModify(MemoModifyDto memoModifyDto);

    public void memoDelete(Long memoId);

    public void likesCreate(String fileId);

    public void likesDelete(String fileId);
}
