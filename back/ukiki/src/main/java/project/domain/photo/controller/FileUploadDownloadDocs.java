package project.domain.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.dto.request.FileDownloadDto;
import project.domain.photo.dto.request.FileUploadDto;
import project.domain.photo.dto.request.MultiFileDownloadDto;
import project.domain.photo.dto.response.GroupbrieflyDto;
import project.global.result.ResultResponse;

import java.util.List;

public interface FileUploadDownloadDocs {

    @Operation(summary = "파일 업로드")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> fileUpload(@RequestPart("files") List<MultipartFile> files,
                                                     @RequestPart("key") @Valid FileUploadDto fileUploadDto) throws Exception;

    @Operation(summary = "단일 파일 다운로드")
    @GetMapping("/download")
    public void fileDownload(FileDownloadDto fileDownloadDto,
                             HttpServletResponse response,
                             @RequestHeader HttpHeaders httpHeaders);

    @Operation(summary = "멀티 파일 다운로드")
    @GetMapping("/multi-select-download")
    public void multiSelectDownload(MultiFileDownloadDto multiFileDownloadDto,
                                    HttpServletResponse response,
                                    @RequestHeader HttpHeaders httpHeaders) throws Exception;

}
