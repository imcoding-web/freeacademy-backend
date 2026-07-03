package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.DegreeService;
import fr.imcoding.edu365.business.services.dataImport.ImportService;
import fr.imcoding.edu365.client.dtos.response.ImportResponse;
import fr.imcoding.edu365.dtos.DegreeDto;
import fr.imcoding.edu365.enumeration.ImportContext;
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

/**
 * @author Rokaya
 * @Date 25/09/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/degree")
@RequiredArgsConstructor
public class DegreeController {
  private final ImportService SpecialityImport;
  private final DegreeService degreeService;

  @GetMapping()
  public List<DegreeDto> getAllDegree() {
    return this.degreeService.getAllDegree();
  }

  @PostMapping(value = "/import",consumes = {"multipart/form-data"})
  public ImportResponse importDegree(@RequestParam(value = "context")ImportContext context,@RequestBody MultipartFile file)
      throws IOException {
    return SpecialityImport.importDataFromExcelFile(context,file);
  }

}
