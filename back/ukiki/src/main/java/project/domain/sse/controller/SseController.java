package project.domain.sse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.sse.service.SseService;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class SseController implements SseDocs{

    private final SseService sseService;

    @Override
    @GetMapping(value= "/sub", produces = "text/event-stream")
    public SseEmitter subScribe(){
        SseEmitter res = sseService.createEmitter();
        return res;
    }

}