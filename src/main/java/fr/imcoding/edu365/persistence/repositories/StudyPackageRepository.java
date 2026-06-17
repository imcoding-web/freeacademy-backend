package fr.imcoding.edu365.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.imcoding.edu365.persistence.entities.StudyPackage;

@Repository
public interface StudyPackageRepository extends JpaRepository<StudyPackage, Long> {

}
