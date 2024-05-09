package project.domain.alarm;

import java.util.ArrayList;
import java.util.List;


public enum AlarmIdentifier {
    PARTY{
        public List<String> identifier(Long partyId, Long detailId){
            return new ArrayList<>(){{
                add("PARTY URL");
            }};
        }
    },
    CHAT{
        public List<String> identifier(Long partyId, Long detailId) {
            return new ArrayList<>(){{
                add("CHAT URL");
            }};
        }
    },
    ARTICLE{
        public List<String> identifier(Long partyId, Long detailId) {
            return new ArrayList<>(){{
                add("ARTICLE URL");
            }};
        }
    },
    COMMENT{
        public List<String> identifier(Long partyId, Long detailId) {
            return new ArrayList<>(){{
                add("COMMENT URL");
            }};
        }
    },
    REPLY{
        public List<String> identifier(Long partyId, Long detailId) {
            return new ArrayList<>(){{
                add("REPLY URL");
            }};
        }
    },

    CHECK{
        public List<String> identifier(Long partyId, Long detailId) {
            return new ArrayList<>(){{
                add("CHECK URL1");
                add("CHECK URL2");
            }};
        }
    },
    ;


    public abstract List<String> identifier(Long partyId, Long detailId);
}
