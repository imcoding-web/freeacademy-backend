package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserEmail(String email);
  Optional<User> findByUserPhoneNumber(String phoneNumber);
  List<User> findByUserRoleRoleCode(RoleCode roleCode);
  Optional<User> findByUuid(UUID uuid);


}
