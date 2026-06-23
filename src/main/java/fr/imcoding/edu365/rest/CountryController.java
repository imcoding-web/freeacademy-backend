package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.CountryService;
import fr.imcoding.edu365.persistence.entities.Country;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {

  private final CountryService countryService;

  @GetMapping()
  public List<Country> getAllCountries() {
    return this.countryService.getAllCountries();
  }

}
