package project.domain.alarm;

import java.util.ArrayList;
import java.util.List;


public enum AlarmIdentifier {
    MEMO{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add(String.format("album/detail/%s/%d", contentsId, partyId));
            }};
        }
    },
    COMMENT{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },
    REPLY{ // 이건 만들어 봐야할듯
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },

    CHECK{ // SSE 연결 체크용
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },
    PASSWORD{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },
    CHAT{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },
    MENTION{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("/chat");
            }};
        }
    },

    ;


    public abstract List<String> identifier(Long partyId, Object contentsId, Object targetId);
//    public abstract List<String> identifier2 (Long partyId, Long detailId);
}
