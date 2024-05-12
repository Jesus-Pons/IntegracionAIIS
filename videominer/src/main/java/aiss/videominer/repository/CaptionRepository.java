package aiss.videominer.repository;

import aiss.videominer.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptionRepository extends JpaRepository<Caption,String> {
}
