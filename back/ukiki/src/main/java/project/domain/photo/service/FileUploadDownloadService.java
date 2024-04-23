package project.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileUploadDownloadService {
    public void fileUpload(List<MultipartFile> files, String key) throws Exception;
}
