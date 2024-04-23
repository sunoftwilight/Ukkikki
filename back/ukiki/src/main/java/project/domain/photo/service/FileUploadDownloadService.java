package project.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadDownloadService {
    public void fileUpload(MultipartFile[] files);
}
