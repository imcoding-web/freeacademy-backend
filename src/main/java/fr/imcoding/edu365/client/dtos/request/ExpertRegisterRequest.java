package fr.imcoding.edu365.client.dtos.request;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

import fr.imcoding.edu365.enumeration.YesNo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 29/09/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertRegisterRequest {
  private UUID userUuid;
  private String userEmail;
	private String userPhoneNumber;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private String userIdentityNumber;

	//private MultipartFile userIdentityFile;
	private MultipartFile userPicture;
	private MultipartFile userDegreeFile;
	private MultipartFile userCVFile;
	private String isTermsAccepted;
	
	//Est ce que l'ensignant fait l'ensignement actuellemnt ou non?
	private YesNo didYouTeach;
	//L'ecole où il ensigne
	private String currentSchool;
	// Le niveau enseigné
	private String currentLevel;
	//La matiére ensigné
	private String currentCourse;
	
	//Est ce que l'ensignant a fait une experience dans l'education en ligne?
	private YesNo previousOnlineTeaching;
	// Les platformes en ligne avec lequels il a travaillé
	private String previousOnlinePlatforms;
	
	//Est ce que l'ensignant a fait avant des cours de support?
	private YesNo previousSupportCourses;
	//Le niveau ensigné lors de ces cours de support
	private String supportCourseLevel;
	//La matiére ensigné lors de ces cours de support
	private String supportCourseCourse;
	
	//présentation de l'ensignant
	private String description;
	
	public boolean isTermsAccepted() {
		return this.isTermsAccepted.equalsIgnoreCase("true");
	}
}
