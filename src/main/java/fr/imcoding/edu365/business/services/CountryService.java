package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.persistence.entities.Country;
import fr.imcoding.edu365.persistence.repositories.CountryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {

  private final CountryRepository countryRepository;

  public List<Country> getAllCountries() {
    return this.countryRepository.findAll();
  }

  public Country saveCountry(Country country) {
    return this.countryRepository.save(country);
  }

  public void deleteCountry(Long id) {
    this.countryRepository.deleteById(id);
  }
  public Country getCountryByCode(String countryCode) {
    return this.countryRepository.findByCountryCode(countryCode);
  }

}
