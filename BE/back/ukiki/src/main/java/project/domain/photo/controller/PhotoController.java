package project.domain.photo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.response.MemoListDto;
import project.domain.photo.service.PhotoService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController implements PhotoDocs {

    private final PhotoService photoService;

    @Override
    @GetMapping("memo/{fileId}")
    public ResponseEntity<ResultResponse> memo(@PathVariable("fileId") String fileId) {

        List<MemoListDto> memos = photoService.memo(fileId);

        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_MEMO_LIST_SUCCESS, memos));
    }

    @Override
    @PostMapping("/memo/create/{fileId}")
    public ResponseEntity<ResultResponse> memoCreate(@PathVariable("fileId") String fileId, @RequestBody MemoDto memoDto) {
        photoService.memoCreate(fileId, memoDto);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.CREATE_MEMO_SUCCESS));
    }

    @Override
    @PostMapping("/memo/modify/{memoId}")
    public ResponseEntity<ResultResponse> memoModify(@PathVariable("memoId") Long memoId, @RequestBody MemoDto memoDto) {
        photoService.memoModify(memoId, memoDto);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.MODIFY_MEMO_SUCCESS));
    }

    @Override
    @DeleteMapping("/memo/delete/{memoId}")
    public ResponseEntity<ResultResponse> memoDelete(@PathVariable("memoId") Long memoId) {

        photoService.memoDelete(memoId);

        return ResponseEntity.ok().body(new ResultResponse(ResultCode.DELETE_MEMO_SUCCESS));
    }

    @Override
    @PostMapping("/likes/{fileId}")
    public ResponseEntity<ResultResponse> likesCreate(@PathVariable("fileId")  String fileId) {
        log.info("come in likesCreate controller");
        photoService.likesCreate(fileId);
        log.info("result controller = void");
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_LIKES_SUCCESS));
    }

    @Override
    @DeleteMapping("/likes/{fileId}")
    public ResponseEntity<ResultResponse> likesDelete(@PathVariable("fileId")  String fileId) {
        log.info("come in likesDelete");
        photoService.likesDelete(fileId);
        log.info("result controller = void");
        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_LIKES_SUCCESS));
    }
}
