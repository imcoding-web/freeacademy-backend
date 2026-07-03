package fr.imcoding.edu365.business.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.dtos.PrestationFeedbackDto;
import fr.imcoding.edu365.persistence.entities.Prestation;
import fr.imcoding.edu365.persistence.entities.PrestationFeedback;
import fr.imcoding.edu365.persistence.repositories.PrestationFeedbackRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/01/2023
 */

@Service
@RequiredArgsConstructor
public class PrestationFeedbackService {
  private final PrestationFeedbackRepository expertRateRepository;
  private final PrestationService prestationService;


  public void addPrestationExpertRate(UUID prestationUuid,PrestationFeedbackDto expertRate){
    Prestation prestation=prestationService.getByUuid(prestationUuid);
    PrestationFeedback prestationExpertRate=new PrestationFeedback();
    prestationExpertRate.setPrestation(prestation);
    prestationExpertRate.setRate(expertRate.getRate());
    prestationExpertRate.setMessage(expertRate.getMessage());
    expertRateRepository.save(prestationExpertRate);

  }

}
