package project.domain.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.chat.entity.Chat;


import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long>{
    Page<Chat> findAllByPartyId(Long partyId, Pageable pageable);
    void deleteAllByPartyId(Long partyId);
    List<Chat> findAllByPartyId(Long partyId);

    List<Chat> findAllByPartyIdAndMemberId(Long partyId, Long profileId);
}
