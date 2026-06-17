package fr.imcoding.edu365.client.dtos.request;

import fr.imcoding.edu365.dtos.MediaDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestationRequest {

  private UUID prestationUuid;
  private List<MultipartFile> files;
  private List<MediaDto> medias;


}
