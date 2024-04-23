package project.domain.photo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.service.FileUploadDownloadService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadDownloadController implements FileUploadDownloadDocs{

    private final FileUploadDownloadService fileUploadDownloadService;

    @PostMapping("/upload")
    public ResponseEntity<?> fileUpload(@RequestParam("files") List<MultipartFile> files, @RequestParam("key") String key) throws Exception {
        fileUploadDownloadService.fileUpload(files, key);

        return null;
    }

}
