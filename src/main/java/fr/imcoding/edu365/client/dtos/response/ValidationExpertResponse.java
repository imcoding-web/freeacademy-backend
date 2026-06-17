package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.PositionDto;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.YesNo;
import fr.imcoding.edu365.persistence.entities.Degree;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ValidationExpertResponse {
    private String identifier;
    private UUID validation_uuid;
    private String currentGraduation;
    private  String identityNumber;
    private  String identityType;
    private  String lastGraduation;
    private  String lastGraduationYear;
    private  String userFirstName;
    private  String userlastName;
    private  UUID  userUuid;
    private  String  userEmail;
    private  String  userPhoneNumber;
    private AccountStatus accountStatus;
    private boolean isValidatedAccount;
    private MediaDetails certificateDocument;
    private MediaDetails cvDocument;
    private MediaDetails graduationDocument;
    private MediaDetails identityDocument;
    private MediaDetails identityPicture;
    private MediaDetails profilePicture;
    private PositionDto position;
    private SpecialityDto speciality;
    private String customSpeciality;
    private String customPosition;
    
	private YesNo didYouTeach;
	private String currentSchool;
	private SkillAreaDto currentLevel;
	private SkillDto currentCourse;
	private YesNo previousOnlineTeaching;
	private String previousOnlinePlatforms;
	private YesNo previousSupportCourses;
	private SkillAreaDto supportCourseLevel;
	private SkillDto supportCourseCourse;
	private String description;

}
