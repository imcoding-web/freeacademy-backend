package fr.imcoding.edu365.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.imcoding.edu365.persistence.entities.TeacherRegisterRequest;

@Repository
public interface TeacherRegisterRequestRepository extends JpaRepository<TeacherRegisterRequest, Long> {
	Optional<TeacherRegisterRequest> findByUserEmail(String email);
	Optional<TeacherRegisterRequest> findByUuid(UUID uuid);
}
