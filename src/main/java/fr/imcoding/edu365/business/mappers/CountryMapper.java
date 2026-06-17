package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.CountryDto;
import fr.imcoding.edu365.persistence.entities.Country;
import fr.imcoding.edu365.persistence.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryMapper {

  private final CountryRepository countryRepository;

  public Country toCountry(CountryDto countryRequest) {
    return countryRepository.findByCountryCode(countryRequest.getCountryCode());

  }

  public CountryDto toCountryDto(Country country) {
    return country != null ? new CountryDto(country.getCountryLabel(), country.getCountryCode()) : null;
  }
}
