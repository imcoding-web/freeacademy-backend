package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.Speciality;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */
public interface SpecialityRepository extends JpaRepository<Speciality,Long> {

  Speciality findByUuid(UUID uuid);

  Speciality findBySpecialityCodeOrderBySpecialityLabel(String specialityCode);

 /* @Query("SELECT s FROM Position p JOIN p.specialities s WHERE p.positionCode =?1")
  List<Speciality> findSpecialitiesPositionCode(String codePisition);
*/

}
