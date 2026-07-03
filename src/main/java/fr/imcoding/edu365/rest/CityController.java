package fr.imcoding.edu365.rest;


import fr.imcoding.edu365.business.services.CityService;
import fr.imcoding.edu365.business.services.dataImport.ImportService;
import fr.imcoding.edu365.client.dtos.response.ImportResponse;
import fr.imcoding.edu365.persistence.entities.City;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {

  private final CityService cityService;
  private final ImportService importService;


  @GetMapping()
  public List<City> cities(
      @RequestParam(value = "country-code", required = false) String countryCode) {
    return cityService.getCitiesByCountryCode(countryCode);

  }

  @PostMapping(value = "/import",consumes = {"multipart/form-data"})
  public ImportResponse importCitiesByCountry(@RequestParam(value = "country-code")String countryCode,@RequestBody MultipartFile file)
      throws IOException {
    return importService.importCitiesFromExcelFile(countryCode,file);
  }

}
