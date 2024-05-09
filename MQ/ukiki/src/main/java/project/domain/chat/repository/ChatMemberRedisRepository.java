package project.domain.chat.repository;

import org.springframework.data.repository.CrudRepository;
import project.domain.chat.redis.ChatMember;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRedisRepository extends CrudRepository<ChatMember, Long> {
        List<ChatMember> findAllByDestination(String destination);
//        List<ChatMember> findAllByDestination(String destination);

        Optional<ChatMember> findByMemberId(Long memberId);
}
