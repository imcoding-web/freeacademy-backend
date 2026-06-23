package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.CityDto;
import fr.imcoding.edu365.persistence.entities.City;
import fr.imcoding.edu365.persistence.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityMapper {


  private final CityRepository cityRepositoty;
  private final CountryMapper countryMapper;

  public City toCity(CityDto cityRequest) {
    return cityRequest != null ? cityRepositoty.findByCityCode(cityRequest.getCityCode()) : null;
  }

  public CityDto toCityDto(City city) {
    return city != null ? new CityDto(city.getCityLabel(), city.getCityCode(),
        countryMapper.toCountryDto(city.getCountry())) : null;
  }
}
