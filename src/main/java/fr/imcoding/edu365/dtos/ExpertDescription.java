package fr.imcoding.edu365.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 27/06/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertDescription {

  private String description;
  private MediaDetails userCoverPicture;
  private MediaDetails userVideo;
  private MediaDetails userVideoPresentation;




}
