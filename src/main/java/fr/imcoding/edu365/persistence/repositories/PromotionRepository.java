package fr.imcoding.edu365.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
	Promotion findByPromotionCode(String promotionCode);
	Promotion findByPromotionCodeAndPromotionType(String promotionCode, TransactionType promotionType);
}
