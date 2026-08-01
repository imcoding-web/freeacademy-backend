package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.mappers.VerifiedExpertMapper;
import fr.imcoding.edu365.business.services.VerifiedExpertService;
import fr.imcoding.edu365.client.dtos.response.ValidationExpertResponse;
import fr.imcoding.edu365.dtos.PageDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 08/11/2022
 */
@RestController
@CrossOrigin(origins = "http://localhost:4201", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/verified-expert")
@RequiredArgsConstructor
public class VerifiedExpertController {
  private final VerifiedExpertService verifiedExpertService;
  private final VerifiedExpertMapper verifiedExpertMapper;


  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping()
  public ValidationExpertResponse getExpertValidationByUuid(@RequestParam("validation-uuid") UUID validationUuid){
    return verifiedExpertMapper.toValidationExpertResponse(verifiedExpertService.findByUuid(validationUuid));
  }




}
