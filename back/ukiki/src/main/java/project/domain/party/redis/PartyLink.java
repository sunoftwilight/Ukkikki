package project.domain.party.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import project.domain.party.entity.Party;
import project.global.baseEntity.BaseEntity;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "partyLink")
public class PartyLink extends BaseEntity {

    @Id
    private String partyLink;

    private Party party;

    @TimeToLive
    @Builder.Default
    private long ttl = 7200;

    @Builder.Default
    int count = 403;

}
