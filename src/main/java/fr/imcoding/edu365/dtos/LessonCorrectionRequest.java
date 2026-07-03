package fr.imcoding.edu365.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonCorrectionRequest {
  private UUID lessonCorrectionUuid;
  private String description ;
  private boolean onlySubscribedUsers;
  private List<MultipartFile> files = new ArrayList<>();
  private UUID lessonUuid;
  private List<MediaDto> medias;


}
