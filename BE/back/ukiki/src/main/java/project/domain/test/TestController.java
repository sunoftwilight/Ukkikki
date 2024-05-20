package project.domain.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.dto.response.GroupbrieflyDto;
import project.domain.photo.service.PhotoGroupServiceImpl;
import project.global.util.S3Util;
import project.global.util.gptutil.GptUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final GptUtil gptUtil;
    private final S3Util s3Util;
    private final PhotoGroupServiceImpl photoGroupServiceImpl;

    @GetMapping("/test/post_thread")
    public String testController1() throws Exception {
        String threadId = gptUtil.postThread();
        return threadId;
    }

    @GetMapping("/test/post_message")
    public String testController2() throws Exception {
        String messageId = gptUtil.postMessage("thread_hVsRfknD3K6Jw4iThHmjaHAJ", "기분이 어때?");
        return messageId;
    }

    @GetMapping("/test/get_message_list")
    public String testController3() throws Exception {
        String response = gptUtil.getMessageList("thread_hVsRfknD3K6Jw4iThHmjaHAJ");
        return response;
    }

    @GetMapping("/test/post_run")
    public String testController4() throws Exception {
        String response = gptUtil.postRun("thread_hVsRfknD3K6Jw4iThHmjaHAJ");
        return response;
    }

    @GetMapping("/test/get_run_status")
    public String testController5() throws Exception {
        String response = gptUtil.getRunStatus("thread_hVsRfknD3K6Jw4iThHmjaHAJ", "run_oU0Q2laYw3DSNSEWCtP99Gzo");
        return response;
    }

    @GetMapping("/test/get_message")
    public String testController6() throws Exception {
        String response = gptUtil.getMessage("thread_hVsRfknD3K6Jw4iThHmjaHAJ", "run_8YAIKFVTNrcEJfb7GWNinkCN");
        return response;
    }

    @GetMapping("/test/post_assistance")
    public String testController7() throws Exception {
        String response = gptUtil.postAssistance();
        return response;
    }

    @PostMapping("/test/file-key-change")
    public String testController8(String oldKey, String newKey, String filename) throws Exception {
        return s3Util.changeKey(oldKey, newKey, filename);
    }

    @GetMapping("/test/key")
    public String testController9(String key) throws Exception {
        return s3Util.generateSSEKey(key);
    }

    @PostMapping("/test/file-expier")
    public void testController8(String key, String filename) throws Exception {
        s3Util.fileExpire(key, filename);
    }

    @PostMapping("/test/file-undo")
    public void testController9(String key, String filename) throws Exception {
        s3Util.fileUndo(key, filename);
    }

    @GetMapping("/test/photo-group")
    public List<GroupbrieflyDto> testController10() throws Exception {
        return photoGroupServiceImpl.getGroups(1L);
    }

    @GetMapping("/test/file/down")
    public byte[] testController11(@RequestParam("key")String key, @RequestParam("filename")String filename) throws Exception {
        return s3Util.fileDownload(key, filename).getObjectContent().readAllBytes();
    }

    @PostMapping("/test/file/upload")
    public String testController123(@RequestPart("file") MultipartFile file, @RequestPart("key") String key){
        return s3Util.fileUpload(file, key);
    }
}
