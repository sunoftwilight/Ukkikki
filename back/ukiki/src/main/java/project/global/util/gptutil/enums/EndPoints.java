package project.global.util.gptutil.enums;

public enum EndPoints {
    POST_THREAD("/threads"),
    POST_ASSISTANCE("/assistants"),
    GET_MESSAGE_LIST("/threads/{thread_id}/messages"),
    POST_MESSAGE("/threads/{thread_id}/messages"),
    DELETE_ASSISTANT("/assistants/{assistant_id}"),
    POST_RUN("/threads/{thread_id}/runs"),
    GET_RUN_STATUS("/threads/{thread_id}/runs/{run_id}"),

    ;

    private static final String BASE_URL = "https://api.openai.com/v1";
    private String uri;


    EndPoints(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return BASE_URL + this.uri;
    }

    // 동적 파라미터를 받아 URL을 생성하는 메서드
    public String getUrlWithParams(String... params) {
        String processedUrl = this.uri;
        for (String param : params) {
            processedUrl = processedUrl.replaceFirst("\\{[^}]*\\}", param);
        }
        return BASE_URL + processedUrl;
    }

    // 쿼리 스트링을 추가하는 메서드
    public String getUrlWithQueryString(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Keys and values must be in pairs");
        }
        StringBuilder queryString = new StringBuilder();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            if (queryString.length() > 0) {
                queryString.append("&");
            } else {
                queryString.append("?");
            }
            queryString.append(keyValuePairs[i]).append("=").append(keyValuePairs[i + 1]);
        }
        return this.getUrl() + queryString;
    }
}
