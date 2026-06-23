package fr.imcoding.edu365.client.dtos.request;

import fr.imcoding.edu365.dtos.DegreeDto;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.PositionDto;
import fr.imcoding.edu365.dtos.SpecialityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 13/06/2022
 */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class PositionRequest  {
    private PositionDto userPosition;
    private SpecialityDto userSpeciality;
    private String lastGraduationYear;
    private DegreeDto currentGraduation;
    private DegreeDto lastGraduation;
    private String customSpeciality;
    private String customPosition;
    private MediaDetails cvDocument;
    private MediaDetails certificateDocument;
    private MediaDetails graduationDocument;

}



