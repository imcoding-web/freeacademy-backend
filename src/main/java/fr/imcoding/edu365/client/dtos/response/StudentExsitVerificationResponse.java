package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.dtos.SkillAreaDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExsitVerificationResponse {

	private boolean exist;
	private String userFirstName;
	private String userLastName;
	private String userFullName;
	private SkillAreaDto skillLevel;
	private UUID userUuid;

}
