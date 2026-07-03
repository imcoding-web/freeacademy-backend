package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationGiverRepository extends JpaRepository<InformationGiver, Long> {
  List<InformationGiver> findBySkillsId(Long skillid);
  List<InformationGiver> findBySkillsSkillCode(String skillCode);
  List<InformationGiver> findByAccountStatus(AccountStatus accountStatus);
  Optional<InformationGiver> findByUserEmail(String email);
  Optional<InformationGiver> findByUuid(UUID uuid);
}
