package project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.dto.MQDto;
import project.result.ResultCode;
import project.result.ResultResponse;
import project.service.MQService;

@RestController
@RequiredArgsConstructor
public class MQController {

    private final MQService mqService;

    /*
    백 서버에서 요청이 들어오면 메모리 큐에 넣어서 관리해줄 예정.
    @params MultipartFile file;
    @params String partyId;
    @params String key;
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> fileUpload(
            @RequestPart("file")MultipartFile multipartFile,
            @RequestPart("partyId") String partyId,
            @RequestPart("key") String key,
            @RequestPart("photoId") Long photoId
            ){


        MQDto mqDto = new MQDto();
        mqDto.setFile(multipartFile);
        mqDto.setPartyId(partyId);
        mqDto.setKey(key);
        mqDto.setPhotoId(photoId);
        mqService.fileUpload(mqDto);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.FILE_UPLOAD_SUCCESS));
    }

//    @PostMapping("/finish")
//    public ResponseEntity<ResultResponse> finish(int index){
//
//        mqService.finish(index);
//
//        return ResponseEntity.ok().body(new ResultResponse(ResultCode.FILE_UPLOAD_SUCCESS));
//    }
//
//    @PostMapping("/size")
//    public ResponseEntity<ResultResponse> size(){
//
//        mqService.queSize();
//
//        return ResponseEntity.ok().body(new ResultResponse(ResultCode.FILE_UPLOAD_SUCCESS));
//    }

}
