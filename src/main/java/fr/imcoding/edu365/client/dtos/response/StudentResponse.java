package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.dtos.SkillAreaDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {
  private UUID userUuid;
  private String userFirstName;
  private String userLastName;
  private String userFullName;
  private SkillAreaDto skillArea;



}
