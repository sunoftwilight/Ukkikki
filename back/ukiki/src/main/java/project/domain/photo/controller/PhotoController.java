package project.domain.photo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.request.MemoModifyDto;
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

        photoService.memoCreate(memoDto);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.CREATE_MEMO_SUCCESS));
    }

    @Override
    @PostMapping("/memo/modify")
    public ResponseEntity<ResultResponse> memoModify(MemoModifyDto memoModifyDto) {

        photoService.memoModify(memoModifyDto);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.MODIFY_MEMO_SUCCESS));
    }

    @Override
    @DeleteMapping("/memo/delete")
    public ResponseEntity<ResultResponse> memoDelete(Long memoId) {

        photoService.memoDelete(memoId);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.DELETE_MEMO_SUCCESS));
    }

    @Override
    @PostMapping("/{fileId}/likes")
    public ResponseEntity<ResultResponse> likesCreate(@PathVariable String fileId) {
        log.info("come in likesCreate controller");
        photoService.likesCreate(fileId);
        log.info("result controller = void");
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_LIKES_SUCCESS));
    }

    @Override
    @DeleteMapping("/{fileId}/likes")
    public ResponseEntity<ResultResponse> likesDelete(String fileId) {
        log.info("come in likesDelete");
        photoService.likesDelete(fileId);
        log.info("result controller = void");
        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_LIKES_SUCCESS));
    }
}
