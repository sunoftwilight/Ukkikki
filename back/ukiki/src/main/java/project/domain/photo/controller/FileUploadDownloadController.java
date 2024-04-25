package project.domain.photo.controller;

import com.amazonaws.services.s3.model.S3Object;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.service.FileUploadDownloadService;

@RestController
@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadDownloadController implements FileUploadDownloadDocs{

    private final FileUploadDownloadService fileUploadDownloadService;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<?> fileUpload(@RequestParam("files") List<MultipartFile> files, @RequestParam("key") String key, @RequestParam("partyId") int partyId) throws Exception {
        fileUploadDownloadService.uploadProcess(files, key, partyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download")
    public void fileDownload(@RequestParam("key") String key, @RequestParam("fileId") long fildId, @RequestParam("saveName") String saveName,HttpServletResponse response) {
        S3Object object = fileUploadDownloadService.fileDownload(key, fildId);
        String contentType = object.getObjectMetadata().getContentType().split("/")[1];
        InputStream inputStream = object.getObjectContent();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + saveName + "." + contentType + "\"");
        response.setHeader("content-transfer-encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", String.valueOf(object.getObjectMetadata().getContentLength()));
        response.setHeader("pragma", "no-cache");
        response.setHeader("Expires", "-1");

        try {
            OutputStream out = response.getOutputStream();
            int readCount = 0;
            byte[] buffer = new byte[1024];
            // 파일 읽을 만큼 크기의 buffer를 생성한 후
            while ((readCount = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, readCount);
                // outputStream에 씌워준다
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
