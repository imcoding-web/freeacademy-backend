package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.Position;
import fr.imcoding.edu365.persistence.entities.Speciality;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
  Position findByUuid(UUID uuid);

  Position findByPositionCodeOrderByPositionLabel(String positionCode);

}
