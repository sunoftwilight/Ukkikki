package project.domain.party.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Index;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import project.domain.party.entity.Party;
import project.global.baseEntity.BaseEntity;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "partyLink")
public class PartyLink {

    @Id
    private String partyLink;
    private String partyName;

    @Indexed
    private Long party;

    @TimeToLive
    @Builder.Default
    private long ttl = 1000 * 3600 * 4;

    @Builder.Default
    int count = 403;

}
