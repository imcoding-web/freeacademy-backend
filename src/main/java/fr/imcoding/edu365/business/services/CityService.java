package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.CityMapper;
import fr.imcoding.edu365.dtos.CityDto;
import fr.imcoding.edu365.persistence.entities.City;
import fr.imcoding.edu365.persistence.repositories.CityRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {

  private final CityRepository cityRepository;

  private final CityMapper cityMapper;

  public List<CityDto> getAllCities() {
    return this.cityRepository.findByCountryCountryCode("TN").stream()
        .map(city -> cityMapper.toCityDto(city)).collect(Collectors.toList());
  }


  public List<City> getCitiesByCountryCode(String countryCode) {
    if (countryCode != null) {
      return this.cityRepository.findByCountryCountryCode(countryCode).stream().sorted(Comparator.comparing(City::getCityLabel)).collect(Collectors.toList());
    } else {
      return cityRepository.findAll().stream().sorted(Comparator.comparing(City::getCityLabel)).collect(Collectors.toList());
    }
  }
  public City getCitieByCode(String cityCode) {

      return this.cityRepository.findByCityCode(cityCode);

  }

  public City saveCity(City City) {
    return this.cityRepository.save(City);
  }

  public void deleteCity(Long id) {
    this.cityRepository.deleteById(id);
  }

}
