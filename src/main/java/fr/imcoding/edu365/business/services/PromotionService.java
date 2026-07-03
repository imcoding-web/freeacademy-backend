package fr.imcoding.edu365.business.services;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.Promotion;
import fr.imcoding.edu365.persistence.repositories.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionService {

	private final PromotionRepository promotionRepository;

	public Promotion findByCode(String promotionCode) {
		return promotionRepository.findByPromotionCode(promotionCode);

	}
	public Promotion findByCodeAndType(String promotionCode, TransactionType promotionType) {
		return promotionRepository.findByPromotionCodeAndPromotionType(promotionCode, promotionType);

	}
	
	public boolean promotionCodeIsValid(String promotionCode, TransactionType promotionType) {
		Promotion promotion = findByCodeAndType(promotionCode, promotionType);
		if (promotion == null ) return false;
		if(promotionType==TransactionType.USER_FEES) {
			return promotion.isValid();
		}
		//autre traitement pour les promotions qui concernent un seul utilsiateur
		return true;
	}

}
