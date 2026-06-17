package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

  Country findByCountryCode(String countryCode);
}
