package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.photo.entity.Meta;

public interface MetaRepository extends JpaRepository<Meta, Long> {

}
