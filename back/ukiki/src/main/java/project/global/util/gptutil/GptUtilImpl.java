package project.global.util.gptutil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static String instruction =
        "사진 유형에 맞는 숫자를 응답해,"
        + "PORTRAIT = 100, INDIVIDUAL = 101, GROUP = 102, FULL_BODY_SHOT = 103, CANDID = 104, ENVIRONMENTAL_PORTRAIT= 105, "
        + "WILDLIFE = 200, PET = 201, BIRD = 202, MICRO_ANIMAL = 203,"
        + "LANDSCAPE = 300, SEASCAPE = 301, ASTRO = 302, CITYSCAPE = 303,"
        + "WESTERN_FOOD = 400, KOREAN_FOOD = 401, CHINESE_FOOD = 402, JAPANESE_FOOD = 403,"
        + "NON_CLASSIFICATION = 500"
        + "단 List 형식으로 응답해 think step by step";

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
    public List<Integer> postChat(String imageUrl) throws Exception{
        // ObjectMapper 인스턴스 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // content 리스트 생성 (내부에 text와 image_url 객체 포함)
        List<Map<String, Object>> contentList = new ArrayList<>();

        // Text 객체 생성 및 content 리스트에 추가
        Map<String, Object> textContent = new HashMap<>();
        textContent.put("type", "text");
        textContent.put("text", instruction);
        contentList.add(textContent);

        // Image 객체 생성 및 content 리스트에 추가 (올바른 중첩된 구조로)
        Map<String, Object> imageContent = new HashMap<>();
        Map<String, String> imageObject = new HashMap<>();
        imageObject.put("url", imageUrl);
        imageContent.put("type", "image_url");
        imageContent.put("image_url", imageObject); // 중첩된 객체로 'url' 추가
        contentList.add(imageContent);

        // 사용자 메시지 객체 생성
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", contentList);

        // 메시지를 포함하는 리스트 생성
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(userMessage);

        // 최상위 map 생성 및 model, messages, max_tokens 추가
        Map<String, Object> map = new HashMap<>();
        map.put("model", "gpt-4-turbo");
        map.put("messages", messages);
        map.put("temperature", 0.2);
        map.put("frequency_penalty", 0.5); // API가 허용하는 범위 내로 설정
        map.put("max_tokens", 300);

        // JSON 문자열로 변환
        String jsonBody = objectMapper.writeValueAsString(map);

        // 로깅
        System.out.println(jsonBody);

        // 요청 헤더 설정
        HttpEntity<String> request = new HttpEntity<>(jsonBody, this.baseHttpHeader());

        // RestTemplate 생성 및 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
            EndPoints.POST_CHAT.getUrl(),
            request,
            String.class
        );

        // 응답 로깅 및 반환
        System.out.println(response);
        return getMeta(response);
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
    public List<Integer> getMeta(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        JsonNode choicesNode = rootNode.path("choices");

        // 반환할 정수 리스트를 초기화
        List<Integer> contentValues = new ArrayList<>();

        // "choices" 배열을 순회하며 content List화
        if (choicesNode.isArray()) {
            for (JsonNode choice : choicesNode) {
                JsonNode contentNode = choice.path("message").path("content");
                // "content"가 문자열로 정수를 포함하고 있다면, 해당 정수를 리스트에 추가합니다.
                if (!contentNode.isMissingNode() && contentNode.isTextual()) {
                    String content = contentNode.asText();
                    // "content" 문자열을 정수로 변환하여 리스트에 추가합니다.
                    content = content.replaceAll("[\\[\\]]", ""); // 대괄호 제거
                    String[] numbers = content.split(","); // 쉼표로 분할
                    for (String number : numbers) {
                        try {
                            contentValues.add(Integer.parseInt(number.trim())); // 공백 제거 및 정수로 변환하여 추가
                        } catch (NumberFormatException e) {
                            // 숫자로 변환할 수 없는 경우 예외 처리
                            System.err.println("NumberFormatException: " + e.getMessage());
                        }
                    }
                }
            }
        }

        return contentValues;
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
