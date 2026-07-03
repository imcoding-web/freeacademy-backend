package fr.imcoding.edu365.rest;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.PrestationFeedbackService;
import fr.imcoding.edu365.dtos.PrestationFeedbackDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/01/2023
 */
@RestController
@CrossOrigin
@RequestMapping("/expert-rate")
@RequiredArgsConstructor
public class PrestationFeedbackController {

  private final PrestationFeedbackService expertRateService;

  @PostMapping
  public void saveExpertPaymentRequest(@RequestParam(value="prestation-uuid") UUID prestationUuid,@RequestBody PrestationFeedbackDto expertRateDto){
     expertRateService.addPrestationExpertRate(prestationUuid,expertRateDto);
  }

}
