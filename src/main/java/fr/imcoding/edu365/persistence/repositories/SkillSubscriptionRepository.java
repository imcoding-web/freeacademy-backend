package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillSubscription;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SkillSubscriptionRepository extends JpaRepository<SkillSubscription, Long> {
  List<SkillSubscription> findByPackageType(PackageType packageType);

  Optional<SkillSubscription> findByUuid(UUID uuid);

  List<SkillSubscription> findByStudentAndSubscriptionStatus(InformationSeeker student, PackageStatus packageStatus);
}
