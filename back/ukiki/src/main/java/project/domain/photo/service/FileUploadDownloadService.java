package project.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadDownloadService {
    public List<String> fileUpload(List<MultipartFile> files, String inputKey, int partyId) throws Exception;
    public void updateDatabase(int partyId, List<String> urls);
    public void resizeImage(List<MultipartFile> files, String inputKey, int partyId) throws Exception;
}
