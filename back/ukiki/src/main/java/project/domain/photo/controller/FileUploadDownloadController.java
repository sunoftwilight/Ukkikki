package project.domain.photo.controller;

import com.amazonaws.services.s3.model.S3Object;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.dto.request.FileDownloadDto;
import project.domain.photo.dto.request.FileUploadDto;
import project.domain.photo.dto.request.MultiFileDownloadDto;
import project.domain.photo.service.FileUploadDownloadService;

import java.io.*;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadDownloadController implements FileUploadDownloadDocs{

    private final FileUploadDownloadService fileUploadDownloadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> fileUpload(@RequestPart("files") List<MultipartFile> files,
                                        @RequestPart("key") @Valid FileUploadDto fileUploadDto) throws Exception {
        fileUploadDownloadService.uploadProcess(files, fileUploadDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download")
    public void fileDownload(@RequestBody @Valid FileDownloadDto fileDownloadDto, HttpServletResponse response) {
        S3Object object = fileUploadDownloadService.fileDownload(fileDownloadDto);
        String contentType = object.getObjectMetadata().getContentType().split("/")[1];
        InputStream inputStream = object.getObjectContent();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileDownloadDto.getPrefix() + "." + contentType + "\"");
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

    @GetMapping("/multi-select-download")
    public void multiSelectDownload(@RequestBody @Valid MultiFileDownloadDto multiFileDownloadDto, HttpServletResponse response) throws Exception {
        HashMap<String, List<File>> map = (HashMap<String, List<File>>) fileUploadDownloadService.multiFileDownload(multiFileDownloadDto);
        String path = map.keySet().iterator().next();
        List<File> files = map.get(path);
        response.setHeader("Content-type", "application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "ukkikki.zip" + "\"");
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        FileInputStream fis = null;

        for (File file : files) {
            out.putNextEntry(new ZipEntry(file.getName()));
            fis = new FileInputStream(file);
            StreamUtils.copy(fis, out);
            fis.close();
            out.closeEntry();
        }

        out.close();

        File folder = new File(path);
        if (folder.exists()) {
            FileUtils.cleanDirectory(folder);//하위 폴더와 파일 모두 삭제

            if (folder.isDirectory()) {
                folder.delete(); // 대상폴더 삭제
            }
        }
    }
}
