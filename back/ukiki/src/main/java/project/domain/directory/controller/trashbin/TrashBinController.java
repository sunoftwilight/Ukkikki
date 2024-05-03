package project.domain.directory.controller.trashbin;

import org.springframework.http.ResponseEntity;
import project.global.result.ResultResponse;

public interface TrashBinController {
    // 조회
    ResponseEntity<ResultResponse> getTrashBin(Long trashBinId);

    // 쓰래기 복원
    ResponseEntity<ResultResponse> restoreTrash(Long trashBinId, String trashId);


    // 휴지통 비우기
    ResponseEntity<ResultResponse> clearTrashBin();
}
