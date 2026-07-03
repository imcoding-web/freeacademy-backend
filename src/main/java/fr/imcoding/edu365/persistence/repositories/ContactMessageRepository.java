package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 05/12/2022
 */
public interface ContactMessageRepository extends JpaRepository<ContactMessage,Long> {

}
