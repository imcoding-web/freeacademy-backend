package fr.imcoding.edu365.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.imcoding.edu365.persistence.entities.InformationSeekerPackage;

@Repository
public interface InformationSeekerPackageRepository extends JpaRepository<InformationSeekerPackage, Long> {
	InformationSeekerPackage findTopByPackageRelatedStudentUuidOrderByCreatedAtDesc(UUID publisherUuid);
}
