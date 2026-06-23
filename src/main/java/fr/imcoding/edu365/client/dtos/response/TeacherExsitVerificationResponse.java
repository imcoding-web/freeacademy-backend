package fr.imcoding.edu365.client.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherExsitVerificationResponse {

	private boolean exist;
	private String userFirstName;
	private String userLastName;
	private String userFullName;
}
