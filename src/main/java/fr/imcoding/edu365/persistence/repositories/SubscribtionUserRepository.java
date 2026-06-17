package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.SubscribtionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribtionUserRepository extends JpaRepository<SubscribtionUser, Long> {

}
