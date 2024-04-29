package project.domain.photo.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SSECustomerKey;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.dto.request.FileDownloadDto;
import project.domain.photo.dto.request.FileUploadDto;
import project.domain.photo.dto.request.MultiFileDownloadDto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileUploadDownloadService {
    public void uploadProcess(List<MultipartFile> files, FileUploadDto fileUploadDto);
    public S3Object fileDownload(FileDownloadDto fileDownloadDto);
    public Map<String, List<File>> multiFileDownload(MultiFileDownloadDto multiFileDownloadDto);
}
