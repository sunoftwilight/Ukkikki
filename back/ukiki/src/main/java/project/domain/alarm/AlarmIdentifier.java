package project.domain.alarm;

import java.util.ArrayList;
import java.util.List;


public enum AlarmIdentifier {
    MEMO{
        public List<String> identifier(Long partyId, Long detailId, Long targetId){
            return new ArrayList<>(){{
                add(String.format("/api/photo/memo/%d/%d", partyId, detailId));
            }};
        }
    },
    COMMENT{
        public List<String> identifier(Long partyId, Long detailId, Long targetId) {
            return new ArrayList<>(){{
                add(String.format("/api/article/comment/%d/%d/", partyId, detailId));
            }};
        }
    },
    REPLY{ // 이건 만들어 봐야할듯
        public List<String> identifier(Long partyId, Long detailId, Long targetId) {
            return new ArrayList<>(){{
                add("REPLY URL");
            }};
        }
    },

    CHECK{ // SSE 연결 체크용
        public List<String> identifier(Long partyId, Long detailId, Long targetId) {
            return new ArrayList<>(){{
                add("/CHECK/URL");
            }};
        }
    },
    PASSWORD{
        @Override
        public List<String> identifier(Long partyId, Long detailId, Long targetId) {
            return null;
        }
    },
    CHAT{
        @Override
        public List<String> identifier(Long partyId, Long detailId, Long targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },
    MENTION{
        @Override
        public List<String> identifier(Long partyId, Long detailId, Long targetId) {
            return null;
        }
    },

    ;


    public abstract List<String> identifier(Long partyId, Long detailId, Long targetId);
//    public abstract List<String> identifier2 (Long partyId, Long detailId);
}
