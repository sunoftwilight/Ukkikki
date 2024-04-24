package project.domain.party.redis;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import project.domain.party.entity.Party;

import java.time.LocalDateTime;

@Data
@Builder
@RedisHash(value = "partyLink")
public class PartyLink {

    @Id
    private Long partyId;

    private String partyLink;
    private LocalDateTime deadLine;
    @Builder.Default
    int count = 403;

}
