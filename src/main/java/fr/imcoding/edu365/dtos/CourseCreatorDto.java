package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.client.dtos.response.User_Details;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 14/09/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCreatorDto {
  private User_Details userDetails;
  private SkillDto skill;



}
