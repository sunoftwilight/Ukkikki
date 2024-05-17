package project.domain.alarm;

import java.util.ArrayList;
import java.util.List;


public enum AlarmIdentifier {
    MEMO{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add(String.format("/album/detail/%s/%d", contentsId, partyId));
            }};
        }
    },
    COMMENT{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add(String.format("http://localhost:5173/feed/%s/%s", partyId, contentsId));
                add(String.format("https://k10d202.p.ssafy.io/feed/%s/%s", partyId, contentsId));
            }};
        }
    },
    REPLY{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add(String.format("http://localhost:5173/feed/%s/%s", partyId, contentsId));
                add(String.format("https://k10d202.p.ssafy.io/feed/%s/%s", partyId, contentsId));
            }};
        }
    },

    CHECK{ // SSE 연결 체크용
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("http://localhost:5173/chat");
                add("https://k10d202.p.ssafy.io/chat");
            }};
        }
    },
    PASSWORD{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("http://localhost:5173/chat");
                add("https://k10d202.p.ssafy.io/chat");
            }};
        }
    },
    CHAT{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add("http://localhost:5173/chat");
                add("https://k10d202.p.ssafy.io/chat");
            }};
        }
    },
    MENTION{
        @Override
        public List<String> identifier(Long partyId, Object contentsId, Object targetId) {
            return new ArrayList<>(){{
                add(String.format("http://localhost:5173/feed/%s/%s", partyId, contentsId));
                add(String.format("https://k10d202.p.ssafy.io/feed/%s/%s", partyId, contentsId));
            }};
        }
    },

    ;


    public abstract List<String> identifier(Long partyId, Object contentsId, Object targetId);
//    public abstract List<String> identifier2 (Long partyId, Long detailId);
}
