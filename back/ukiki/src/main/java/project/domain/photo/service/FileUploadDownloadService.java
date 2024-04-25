package project.domain.photo.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SSECustomerKey;
import java.awt.image.BufferedImage;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadDownloadService {
    public String fileUpload(MultipartFile file, SSECustomerKey sseKey) throws Exception;
    public void updateDatabase(int partyId, List<String> urls);
    public BufferedImage resizeImage(MultipartFile file, int thumbnailNum) throws Exception;
    public void uploadProcess(List<MultipartFile> files, String inputKey, long partyId) throws Exception;
    public S3Object fileDownload(String inputKey, long partyId);
}
