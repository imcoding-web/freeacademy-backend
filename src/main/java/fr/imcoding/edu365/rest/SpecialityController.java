package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.PositionService;
import fr.imcoding.edu365.business.services.SpecialityService;
import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.persistence.entities.Speciality;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 10/06/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/speciality")
@RequiredArgsConstructor
public class SpecialityController {

  private final SpecialityService specialityService;

 /* @GetMapping()
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  public List<SpecialityDto> specialities(
      @RequestParam(value = "position-code", required = false) String specialityCode) {
    return specialityService.getSpecialityByPositionCode(specialityCode);

  }*/

  @GetMapping()
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  public List<SpecialityDto> specialities() {
    return specialityService.getSpecialities();

  }

}
