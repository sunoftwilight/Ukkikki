package project.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.chat.entity.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long>{
    List<Chat> findAllByPartyId(Long partyId);
    void deleteAllByPartyId(Long partyId);
}
