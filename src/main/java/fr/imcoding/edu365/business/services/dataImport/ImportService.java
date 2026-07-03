package fr.imcoding.edu365.business.services.dataImport;

import fr.imcoding.edu365.business.services.CityService;
import fr.imcoding.edu365.business.services.CountryService;
import fr.imcoding.edu365.business.services.DegreeService;
import fr.imcoding.edu365.business.services.PositionService;
import fr.imcoding.edu365.business.services.SpecialityService;
import fr.imcoding.edu365.client.dtos.response.FailureImportBaseDetails;
import fr.imcoding.edu365.client.dtos.response.ImportResponse;
import fr.imcoding.edu365.enumeration.FailureImportCode;
import fr.imcoding.edu365.enumeration.ImportContext;
import fr.imcoding.edu365.persistence.entities.City;
import fr.imcoding.edu365.persistence.entities.Country;
import fr.imcoding.edu365.persistence.entities.Degree;
import fr.imcoding.edu365.persistence.entities.Position;
import fr.imcoding.edu365.persistence.entities.Speciality;
import fr.imcoding.edu365.utils.ImportUtils;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 25/09/2022
 */
@Service
@RequiredArgsConstructor
public class ImportService {

  private final DegreeService degreeService;
  private final PositionService positionService;
  private final SpecialityService specialityService;
  private final CityService cityService;
  private final CountryService countryService;


  public ImportResponse importDataFromExcelFile(ImportContext context, MultipartFile file)
      throws IOException {

    ArrayList<FailureImportBaseDetails> failureImportBaseDetails = new ArrayList<>();
    XSSFWorkbook workbook = null;
    int emptyLines = 0;
    workbook = new XSSFWorkbook(file.getInputStream());
    XSSFSheet worksheet = workbook.getSheetAt(0);
    int lastr = worksheet.getLastRowNum();

    for (int j = 1; j <= lastr; j++) {
      XSSFRow row = worksheet.getRow(j);
      if (ImportUtils.isRowEmpty(row)) {
        emptyLines++;
        continue;
      }
      if (row != null) {
        String label = "";

        label = row.getCell(0).getStringCellValue().trim();

        switch (context) {
          case DEGREE: {

            Degree degree = degreeService
                .getDegreeByCode(label.toUpperCase().replace(" ", "_"));
            if (degree == null) {
              degreeService.saveDegree(label);

            } else {
              failureImportBaseDetails.add(new FailureImportBaseDetails(
                  worksheet.getRow(j).getCell(0).getStringCellValue().trim(), j + 1,
                  FailureImportCode.EXIST));
              break;
            }
          }
          break;
          case POSITION: {
            Position position = positionService.getByCOde(label.toUpperCase().replace(" ", "_"));
            if (position == null) {
              Position newPosition = new Position();
              newPosition.setPositionLabel(label);
              newPosition.setPositionCode(label.toUpperCase().replace(" ", "_"));
              positionService.savePosition(newPosition);
            } else {
              failureImportBaseDetails.add(new FailureImportBaseDetails(
                  worksheet.getRow(j).getCell(0).getStringCellValue().trim(), j + 1,
                  FailureImportCode.EXIST));
            }
          }
          break;
          case SPECIALITY: {
            Speciality speciality = specialityService
                .getSpecialiteByCode(label.toUpperCase().replace(" ", "_"));
            if (speciality == null) {
              Speciality newSpeciality = new Speciality();
              newSpeciality.setSpecialityLabel(label);
              newSpeciality.setSpecialityCode(label.toUpperCase().replace(" ", "_"));
              specialityService.saveSpeciality(newSpeciality);
            } else {
              failureImportBaseDetails.add(new FailureImportBaseDetails(
                  worksheet.getRow(j).getCell(0).getStringCellValue().trim(), j + 1,
                  FailureImportCode.EXIST));
            }
          }
          break;
        }

      }
    }
    ImportResponse response = new ImportResponse(
        lastr - failureImportBaseDetails.size() - emptyLines, emptyLines,
        failureImportBaseDetails.size(), failureImportBaseDetails);
    return response;
  }


  public ImportResponse importCitiesFromExcelFile(String countyCode, MultipartFile file)
      throws IOException {
    ArrayList<FailureImportBaseDetails> failureImportBaseDetails = new ArrayList<>();
    XSSFWorkbook workbook = null;
    int emptyLines = 0;
    workbook = new XSSFWorkbook(file.getInputStream());
    XSSFSheet worksheet = workbook.getSheetAt(0);
    int lastr = worksheet.getLastRowNum();

    if (countyCode == null) {
      failureImportBaseDetails.add(new FailureImportBaseDetails(countyCode, 0,
          FailureImportCode.EMPTY));
      ImportResponse response = new ImportResponse(0, 0, 0, failureImportBaseDetails);
      return response;
    } else {
      Country country = countryService.getCountryByCode(countyCode);
      if (country == null) {

        failureImportBaseDetails.add(new FailureImportBaseDetails(countyCode, 0,
            FailureImportCode.NOT_FOUND));
        ImportResponse response = new ImportResponse(0, 0, 0, failureImportBaseDetails);
        return response;
      } else {
        for (int j = 1; j <= lastr; j++) {
          XSSFRow row = worksheet.getRow(j);
          if (ImportUtils.isRowEmpty(row)) {
            emptyLines++;
            continue;
          }
          if (row != null) {

            String label = "";

            label = row.getCell(0).getStringCellValue().trim();

            City city = cityService.getCitieByCode(label.toUpperCase().replace(" ", "_"));
            if (city == null) {
              city = new City();
              city.setCityLabel(label);
              city.setCountry(country);
              cityService.saveCity(city);


            } else {
              failureImportBaseDetails.add(new FailureImportBaseDetails(
                  worksheet.getRow(j).getCell(0).getStringCellValue().trim(), j + 1,
                  FailureImportCode.EXIST));
              //break;
            }
          }


        }
      }
    }
    ImportResponse response = new ImportResponse(
        lastr - failureImportBaseDetails.size() - emptyLines, emptyLines,
        failureImportBaseDetails.size(), failureImportBaseDetails);
    return response;
  }
}


