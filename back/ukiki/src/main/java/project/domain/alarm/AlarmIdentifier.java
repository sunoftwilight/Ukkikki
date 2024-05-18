package project.domain.alarm;

import java.util.ArrayList;
import java.util.List;


public enum AlarmIdentifier {
    MEMO{
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return String.format("album/detail/%s/%d", contentsId, partyId);
        }
    },
    COMMENT{
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return String.format("feed/%d/%s", partyId, contentsId);
        }
    },
    REPLY{
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return String.format("feed/%d/%s", partyId, contentsId);
        }
    },

    CHECK{ // SSE 연결 체크용
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return null;
        }
    },
    PASSWORD{
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return null;
        }
    },
    CHAT{
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return String.format("chat/%d", partyId);
        }
    },
    MENTION{
        @Override
        public String identifier(Long partyId, Object contentsId, Object targetId) {
            return String.format("feed/%d/%s", partyId, contentsId);
        }
    },

    ;


    public abstract String identifier(Long partyId, Object contentsId, Object targetId);
}
