package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.dtos.SkillAreaWithPackagesDTO;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */
public interface SkillAreaPackageRepository extends JpaRepository<SkillAreaPackage,Long> {

  List<SkillAreaPackage> findBySkillLevelSkillAreaCode(String skillLevelCode);
  Optional<SkillAreaPackage> findBySkillLevelUuidAndPackageType(UUID uuid,PackageType packageType);



}
