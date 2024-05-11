package project.domain.directory.controller.trashbin;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.domain.directory.dto.response.GetTrashBinDto;
import project.domain.directory.dto.response.GetTrashDto;
import project.domain.directory.service.TrashBinService;
import project.domain.directory.service.TrashService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/trash-bin")
public class TrashBinControllerImpl implements TrashBinController {

    private final TrashBinService trashBinService;
    private final TrashService trashService;


    @Override
    @GetMapping("/{trashBinId}")
    public ResponseEntity<ResultResponse> getTrashBin(
        @PathVariable(name = "trashBinId") Long trashBinId
    ) {
        List<GetTrashDto> response = trashBinService.getTrashBin(trashBinId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_TRASH_BIN_SUCCESS, response));
    }

    @Override
    @PatchMapping("/{trashBinId}/trashes/{trashId}")
    public ResponseEntity<ResultResponse> restoreOneTrash(
        @PathVariable(name = "trashBinId") Long trashBinId,
        @PathVariable(name = "trashId") String trashId
    ) {
        trashService.restoreOneTrash(trashId, trashBinId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.RESTORE_DIRECTORY_OR_FILE_SUCCESS));
    }

    @Override
    public ResponseEntity<ResultResponse> clearTrashBin() {
        return null;
    }


}
