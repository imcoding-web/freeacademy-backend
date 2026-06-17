package fr.imcoding.edu365.client.dtos.request;

import fr.imcoding.edu365.dtos.MediaDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 29/09/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertDescriptionRequest {
  private String description;
  private MultipartFile videoFile;
  private MultipartFile coverPictureFile;
}
