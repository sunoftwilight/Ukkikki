package project.domain.photo.entity;

import lombok.Getter;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

@Getter
public enum MetaCode {
    // 사람
    PORTRAIT(100), INDIVIDUAL(101), GROUP(102), FULL_BODY_SHOT(103), CANDID(
        104), ENVIRONMENTAL_PORTRAIT(105),
    // 동물
    WILDLIFE(200), PET(201), BIRD(202), MICRO_ANIMAL(203),
    // 자연
    LANDSCAPE(300), SEASCAPE(301), ASTRO(302), CITYSCAPE(303),
    // 음식
    WESTERN_FOOD(400), KOREAN_FOOD(401), CHINESE_FOOD(402), JAPANESE_FOOD(403),
    // 기타
    ETC(500);
    private int code;

    MetaCode(int code) {
        this.code = code;
    }

    public static MetaCode getEnumByCode(int code) {
        for (MetaCode mc : MetaCode.values()) {
            if (mc.getCode() == code) {
                return mc;
            }
        }
        throw new BusinessLogicException(ErrorCode.META_CODE_NOT_FOUND);
    }

}
