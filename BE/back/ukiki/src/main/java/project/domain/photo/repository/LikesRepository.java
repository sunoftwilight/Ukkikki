package project.domain.photo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.Member;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.mediatable.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberAndPhoto(Member member, Photo photo);
}
