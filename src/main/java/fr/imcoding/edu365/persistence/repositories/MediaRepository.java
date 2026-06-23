package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.Media;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
  Media findByUuid(UUID uuid);
  List<Media> findByMediaLabel(String mediaLabel);
}
