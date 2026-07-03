package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  City findByCityCode(String cityCode);
  List<City> findByCountryCountryCode(String countryCode);
}
