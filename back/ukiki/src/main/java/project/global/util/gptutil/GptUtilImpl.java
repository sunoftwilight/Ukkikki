package project.global.util.gptutil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.global.util.gptutil.enums.EndPoints;

@Component
@Slf4j
public class GptUtilImpl implements GptUtil {

    private HttpHeaders headers;
    @Value("${openai.api.key}")
    private String openApiKey;

    @Override
    public String postThread() throws Exception{
        // body 생성
//        String jsonBody = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
        String jsonBody = "{}";
        HttpEntity<String> request = new HttpEntity<>(jsonBody, this.baseHttpHeader());

        // 요청 전송 및 반환(json으로 응답이 온다.)
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
            EndPoints.POST_THREAD.getUrl(),
            request,
            String.class
        );

        log.info("(OpenAiUtil) {}", response);

        return this.getId(response);
    }

    @Override
    public String postMessage(String threadId, String content) throws Exception {
        // Body 생성
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("role", "user");
        map.put("content", content);
        String jsonBody = objectMapper.writeValueAsString(map);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, this.baseHttpHeader());

        // 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
            EndPoints.POST_MESSAGE.getUrlWithParams(threadId),
            request,
            String.class

        );
        log.info("(OpenAiUtil) {}", response);
        return this.getId(response);
    }

    @Override
    public String getMessageList(String threadId) throws Exception {
        HttpEntity<String> request = new HttpEntity<>("{}", this.baseHttpHeader());

        RestTemplate restTemplate = new RestTemplate();
        // exchange 메서드(header가 필수인경우)를 사용하여 GET 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(
            EndPoints.GET_MESSAGE_LIST.getUrlWithParams(threadId),
            HttpMethod.GET,
            request,
            String.class
        );

        log.info("(OpenAiUtil) {}", response.getBody());

        return response.getBody();
    }

    @Override
    public String getMessage(String threadId, String runId) throws Exception {
        HttpEntity<String> request = new HttpEntity<>("{}", this.baseHttpHeader());

        RestTemplate restTemplate = new RestTemplate();
        // exchange 메서드(header가 필수인경우)를 사용하여 GET 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(
            EndPoints.GET_MESSAGE_LIST.getUrlWithParams(threadId) + "?run_id=" + runId,
            HttpMethod.GET,
            request,
            String.class
        );

        log.info("(OpenAiUtil) {}", response.getBody());

//        return response.getBody();
        return getMessageContent(response.getBody());
    }

    @Override
    public String postRun(String threadId) throws Exception {
        // Body 생성
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("assistant_id", "asst_wunvOCS3EPQMirgaQ1U66Aqc");
        String jsonBody = objectMapper.writeValueAsString(map);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, this.baseHttpHeader());

        // 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
            EndPoints.POST_RUN.getUrlWithParams(threadId),
            request,
            String.class

        );
        log.info("(OpenAiUtil) {}", response);
        return this.getId(response);
    }

    @Override
    public String getRunStatus(String threadId, String runId) throws Exception{
        HttpEntity<String> request = new HttpEntity<>("{}", this.baseHttpHeader());

        RestTemplate restTemplate = new RestTemplate();
        // exchange 메서드(header가 필수인경우)를 사용하여 GET 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(
            EndPoints.GET_RUN_STATUS.getUrlWithParams(threadId, "run_oU0Q2laYw3DSNSEWCtP99Gzo"),
            HttpMethod.GET,
            request,
            String.class
        );

        log.info("(OpenAiUtil) {}", response.getBody());

//        return response.getBody();
        return getStatus(response.getBody());
    }

    @Override
    public String postAssistance() throws Exception {
        // Body 생성
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("model", "gpt-3.5-turbo");
        map.put("name", "first_ai");
        map.put("description", "첫 번째 어시스턴스");
        map.put("instructions", "json format please");
        map.put("temperature", 0.2);
        map.put("response_format", "auto");
//        map.put("max_tokens", 100);
        String jsonBody = objectMapper.writeValueAsString(map);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, this.baseHttpHeader());

        // 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
            EndPoints.POST_ASSISTANCE.getUrl(),
            request,
            String.class

        );
        log.info("(OpenAiUtil) {}", response);
        return this.getId(response);
    }


    @Override
    public HttpHeaders baseHttpHeader() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        // 헤더 생성
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openApiKey);
        headers.set("OpenAI-Beta", "assistants=v1");
        return headers;
    }

    @Override
    public String getId(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        return rootNode.path("id").asText();
    }

    @Override
    public String getStatus(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        return rootNode.path("status").asText();
    }

    @Override
    public String getMessageContent(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);

        // "data" 배열의 첫 번째 요소에서 "content" 배열의 첫 번째 요소의 "text" 객체의 "value" 필드를 추출
        JsonNode contentNode = rootNode.path("data").get(0).path("content").get(0).path("text").path("value");

        return contentNode.asText();
    }



}
