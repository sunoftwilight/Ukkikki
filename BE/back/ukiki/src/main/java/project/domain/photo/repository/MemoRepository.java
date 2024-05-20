package project.domain.photo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.domain.photo.dto.response.MemoListDto;
import project.domain.photo.entity.mediatable.Memo;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("SELECT new project.domain.photo.dto.response.MemoListDto(m.id, m.content, member.id, member.userName)" +
    "FROM Memo m JOIN m.member member WHERE m.photo.id = :photoId")
    List<MemoListDto> findByPhotoId(@Param("photoId") Long photoId);
}
