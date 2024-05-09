package project.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;


@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    /**
     * Constructor for the converter to support HTTP requests with header Content-Type: multipart/form-data
     */
    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        // 지정된 ObjectMapper와 MULTIPART_FORM_DATA 미디어 타입을 사용하여 컨버터 초기화
        super(objectMapper, MediaType.MULTIPART_FORM_DATA);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        // multipart/form-data 타입의 요청이 읽을 수 있는지 확인
        return mediaType.equals(MediaType.MULTIPART_FORM_DATA);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        // 이 컨버터는 데이터를 쓰지 않음
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        // 이 컨버터는 데이터를 쓰지 않음
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        // 이 컨버터는 데이터를 쓰지 않음
        return false;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // 지원하는 클래스 타입을 결정 (일반적으로 모든 도메인 모델 또는 DTO에 대해 true를 반환)
        return true;
    }
}