package project.global.util.gptutil;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

public interface GptUtil {

    String postThread() throws Exception;
    String postMessage(String threadId, String content) throws Exception;
    String getMessageList(String threadId) throws Exception;
    String getMessage(String threadId, String runId) throws Exception;
    String postRun(String threadId) throws Exception;
    String getRunStatus(String threadId, String runId) throws Exception;
    /**
     * GPT 통신을 위한 공통 Header를 생성하는 메서드
     *
     * @return
     * @throws Exception
     */
    HttpHeaders baseHttpHeader() throws Exception;
    String getId(String response) throws Exception;
    String getStatus(String response) throws Exception;
    String getMessageContent(String response) throws Exception;
    String postAssistance() throws Exception;

    /**
     * GPT 통신 API 호출 메서드
     *
     * @param file : client에서 전달 받은 사진 BinaryData
     * @return : 정수 형태의 사진 분류 code 리스트를 반환 ex) [101, 203, ...]
     * @throws Exception
     */
    List<Integer> postChat(MultipartFile file) throws Exception;

    /**
     * GPT 응답에서 content value 추출하는 메서드
     *
     * @param response : GPT 응답 raw Data
     * @return : 분류코드 리스트
     * @throws Exception
     */
    List<Integer> getMeta(String response) throws Exception;

    /**
     * client에서 전달 받은 Binary jpg 데이터를 Base64로 인코딩
     *
     * @param file : client에서 전달받은 Binary jpg 데이터
     * @return : Base64 인코딩 문자열
     */
    String fileToBase64(MultipartFile file);
}
