package fr.imcoding.edu365.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class LessonCorrectionResponse {
  private UUID lessonCorrectionUuid;
  private String description ;
  private boolean onlySubscribedUsers;
  private List<MediaDetails> medias = new ArrayList<>();

}
