package project.global.util.gptutil;

import java.util.List;
import org.springframework.http.HttpHeaders;

public interface GptUtil {

    String postThread() throws Exception;
    String postMessage(String threadId, String content) throws Exception;
    String getMessageList(String threadId) throws Exception;
    String getMessage(String threadId, String runId) throws Exception;
    String postRun(String threadId) throws Exception;
    String getRunStatus(String threadId, String runId) throws Exception;
    HttpHeaders baseHttpHeader() throws Exception;
    String getId(String response) throws Exception;
    String getStatus(String response) throws Exception;
    String getMessageContent(String response) throws Exception;
    String postAssistance() throws Exception;
    List<Integer> postChat(String imageUrl) throws Exception;
    List<Integer> getMeta(String response) throws Exception;
}
