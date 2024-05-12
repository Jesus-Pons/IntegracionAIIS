package aiss.videominer.repository;

import aiss.videominer.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video,String> {
}
