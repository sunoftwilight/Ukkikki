package project.domain.photo.service;


import project.domain.photo.dto.request.MemoDto;

public interface PhotoService {
    public void memoCreate(MemoDto memoDto);

    public void likesCreate(String fileId);

    public void likesDelete(String fileId);
}
