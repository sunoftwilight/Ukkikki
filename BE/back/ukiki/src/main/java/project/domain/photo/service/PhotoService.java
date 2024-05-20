package project.domain.photo.service;


import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.response.MemoListDto;

import java.util.List;

public interface PhotoService {
    public List<MemoListDto> memo(String fileId);
    public void memoCreate(String fileId, MemoDto memoDto);

    public void memoModify(Long memoId, MemoDto memoDto);

    public void memoDelete(Long memoId);

    public void likesCreate(String fileId);

    public void likesDelete(String fileId);
}
