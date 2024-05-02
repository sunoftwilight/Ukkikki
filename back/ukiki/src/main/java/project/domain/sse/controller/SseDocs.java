package project.domain.sse.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseDocs {

    
    @Operation(summary = "sse 구독하기")
    public SseEmitter subScribe();
}
