package fr.imcoding.edu365.dtos;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 18/08/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarAnnouncementSearchCriteria {
  private String fieldValue;
  private String fieldName;
  private UUID announcementUuid;

}
