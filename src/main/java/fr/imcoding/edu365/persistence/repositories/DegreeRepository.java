package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rokaya
 * @Date 25/09/2022
 */
@Repository
public interface DegreeRepository extends JpaRepository<Degree,Long> {
  Degree findByDegreeCode(String degreeCode);

}
