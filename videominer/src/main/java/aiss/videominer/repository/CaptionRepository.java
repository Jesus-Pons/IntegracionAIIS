package aiss.videominer.repository;

import aiss.videominer.model.Caption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptionRepository extends JpaRepository<Caption,String> {
    Page<Caption> findByLanguage(String language, Pageable paging);
}
