package project.domain.photo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.service.PhotoService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController implements PhotoDocs {

    private final PhotoService photoService;

    @Override
    @PostMapping("/memo/create")
    public ResponseEntity<ResultResponse> memoCreate(MemoDto memoDto) {

        System.out.println(memoDto.getPhotoId());
        photoService.memoCreate(memoDto);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.CREATE_MEMO_SUCCESS));
    }
}
