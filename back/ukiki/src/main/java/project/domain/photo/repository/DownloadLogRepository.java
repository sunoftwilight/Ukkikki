package project.domain.photo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.Member;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.mediatable.DownloadLog;

public interface DownloadLogRepository extends JpaRepository<DownloadLog, Long> {

    Optional<DownloadLog> findByMemberAndPhoto(Member member, Photo photo);

}
