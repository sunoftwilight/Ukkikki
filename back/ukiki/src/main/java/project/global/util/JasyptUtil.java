package project.global.util;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.FixedStringSaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.global.config.JasyptConfig;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JasyptUtil {

    private Map<Long, StringEncryptor> partyEncryptor = new HashMap<>();

    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    private static JasyptConfig jasyptConfig;
    // 주어진 문자열을 암호화하여 반환하는 메서드
    public String encrypt(String input) {
        return jasyptStringEncryptor.encrypt(input);
    }

    // 암호화된 문자열을 복호화하여 반환하는 메서드
    public String decrypt(String encryptedInput) {
        return jasyptStringEncryptor.decrypt(encryptedInput);
    }

    // 사용자 키를 이용한 암호화 알고리즘 생성
    public StringEncryptor customEncryptor(String clientKey){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(clientKey);    //암호화 시 사용할 키 -> 이 키를 가지고 암호화 복호화 진행
        config.setAlgorithm("PBEWithMD5AndDES"); //사용할 알고리즘
        config.setKeyObtentionIterations("1000");   //반복할 해싱 회수
        config.setPoolSize("1");    //pool 크기
        config.setProviderName("SunJCE");   //사용할 암호화 라이브러리
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");    //salt 생성 클래스
        FixedStringSaltGenerator saltGenerator = new FixedStringSaltGenerator();
        saltGenerator.setSalt(clientKey); // 고정된 솔트 값 설정
        config.setSaltGenerator(saltGenerator);

        config.setStringOutputType("base64");   //인코딩 방식
        encryptor.setConfig(config);    //설정 주입

        return encryptor;
    }


    // 암호화 알고리즘을 같이 넣으면 암호화 시켜준다.
    public String keyEncrypt(StringEncryptor keyEncryptor, String text){
        return keyEncryptor.encrypt(text);
    }
    // 암호화 알고리즘을 같이 넣으면 복호화 시켜준다.
    public String keyDecrypt(StringEncryptor keyEncryptor, String text){
        return keyEncryptor.decrypt(text);
    }

    public StringEncryptor getPartyEncryptor(Long partyId, String partyKey){
        StringEncryptor encryptor = partyEncryptor.getOrDefault(partyId, null);
        if(encryptor == null){
            StringEncryptor customEncryptor = customEncryptor(partyKey);
            partyEncryptor.put(partyId, customEncryptor);
            return customEncryptor;
        }
        return encryptor;
    }
}