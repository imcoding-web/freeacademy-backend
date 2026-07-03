package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.PackageSubscription;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
public interface PackageSubscriptionRepository extends JpaRepository<PackageSubscription,Long> {
  List<PackageSubscription>findBySkillAreaPackagePackageType(PackageType packageType);

  Optional<PackageSubscription> findByUuid(UUID uuid);
  //List<PackageSubscription> findByStudentAndSubscriptionStatus(InformationSeeker student, PackageStatus subscriptionStatus);
  boolean existsByStudentAndSubscriptionStatus(InformationSeeker student, PackageStatus subscriptionStatus);

  PackageSubscription findByStudentAndSubscriptionStatus(InformationSeeker student, PackageStatus subscriptionStatus);

    @Query("SELECT ps.student " +
        "FROM PackageSubscription ps " +
        "WHERE ps.subscriptionStatus = :status " +
        "AND ps.student.currentLevel.skillAreaCode = :packageCode " +
        "AND ps.student.currentLevelSection.code = :sectionCode")
    List<InformationSeeker> findActiveStudentsByPackageAndSection(
        @Param("status") PackageStatus status,
        @Param("packageCode") String packageCode,
        @Param("sectionCode") String sectionCode);


}
