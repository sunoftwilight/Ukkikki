package project.domain.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import project.domain.photo.dto.request.MemoDto;
import project.global.result.ResultResponse;

@Tag(name = "MemoController", description = "메모 api")
public interface PhotoDocs {

    @Operation(summary = "메모 등록")
    ResponseEntity<ResultResponse> memoCreate(MemoDto memoDto);
}
