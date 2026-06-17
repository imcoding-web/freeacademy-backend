package fr.imcoding.edu365.business.mappers;

import java.util.Date;

import org.springframework.stereotype.Component;

import fr.imcoding.edu365.business.services.PositionService;
import fr.imcoding.edu365.client.dtos.response.ValidationExpertResponse;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.TeacherRegisterRequest;
import fr.imcoding.edu365.persistence.repositories.InformationGiverRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 17/07/2022
 */
@Component
@RequiredArgsConstructor
public class TeacherRegisterRequestMapper {
    private final  MediaDatailsMapper mediamapper;
    private final PositionService positionService;
    private final SkillAreaMapper skillAreaMapper;
    private final SkillMapper skillMapper;
    private final InformationGiverRepository informationGiverRepository;

    

	public ValidationExpertResponse toValidationExpertResponse(TeacherRegisterRequest request) {

		Media cv = null;
		Media diploma = null;
		Media pictureProfile = null;
		InformationGiver existingTeacher = informationGiverRepository.findByUuid(request.getExistingTeacherUuid())
				.orElse(null);
		cv = existingTeacher == null
				? request.getMedias().stream().filter(media -> media.getMediaContext() == MediaContext.CV_DOCUMENT)
						.findFirst().orElse(null)
				: existingTeacher.getMedias().stream()
						.filter(media -> media.getMediaContext() == MediaContext.CV_DOCUMENT).findFirst().orElse(null);
		diploma = existingTeacher == null ? request.getMedias().stream()
				.filter(media -> media.getMediaContext() == MediaContext.PICTURE_GRADUATION).findFirst().orElse(null)
				: existingTeacher.getMedias().stream()
						.filter(media -> media.getMediaContext() == MediaContext.PICTURE_GRADUATION).findFirst()
						.orElse(null);
		pictureProfile = existingTeacher == null
				? request.getMedias().stream().filter(media -> media.getMediaContext() == MediaContext.PICTURE_PROFIL)
						.findFirst().orElse(null)
				: existingTeacher.getMedias().stream()
						.filter(media -> media.getMediaContext() == MediaContext.PICTURE_PROFIL).findFirst()
						.orElse(null);

		return ValidationExpertResponse
				.builder()
				.identifier(request.getUniqueIdentifier())
				.userFirstName(request.getUserFirstName())
				.userlastName(request.getUserLastName())
				.userEmail(request.getUserEmail())
				.userPhoneNumber(request.getUserPhoneNumber())
				.userUuid(request.getUuid())
				.didYouTeach(request.getDidYouTeach())
				.currentSchool(request.getCurrentSchool())
				.currentLevel(request.getCurrentLevel() != null ? skillAreaMapper.toSkillAreaDto(request.getCurrentLevel()): null)
				.currentCourse(request.getCurrentCourse() != null ? skillMapper.toSkillDto(request.getCurrentCourse()) : null)
				.previousOnlineTeaching(request.getPreviousOnlineTeaching())
				.previousOnlinePlatforms(request.getPreviousOnlinePlatforms())
				.previousSupportCourses(request.getPreviousSupportCourses())
				.supportCourseLevel(request.getSupportCourseLevel() != null ? skillAreaMapper.toSkillAreaDto(request.getSupportCourseLevel()) : null)
				.supportCourseCourse(request.getSupportCourseCourse() != null ? skillMapper.toSkillDto(request.getSupportCourseCourse()) : null)
				.cvDocument(cv != null ? mediamapper.toMediaDetails(cv) : null)
				.graduationDocument(diploma != null ? mediamapper.toMediaDetails(diploma) : null)
				.profilePicture(pictureProfile != null ? mediamapper.toMediaDetails(pictureProfile) : null)
				.description(request.getDescription())
				.build();
	}
	
	public InformationGiver toValidatedInformationGiver(TeacherRegisterRequest request) {

		InformationGiver informationGiver = null;
		if(request.getExistingTeacherUuid() != null) {
			informationGiver = informationGiverRepository.findByUuid(request.getExistingTeacherUuid()).orElse(null);
		} else {
			informationGiver = new InformationGiver();
		}
		if(informationGiver != null) {
			if(request.getExistingTeacherUuid() == null) {
				informationGiver.setMedias(request.getMedias());
				informationGiver.setUserEmail(request.getUserEmail());
				informationGiver.setUserInscriptionDate(request.getCreatedAt());
			}
			
			informationGiver.setUserFirstName(request.getUserFirstName());
			informationGiver.setUserLastName(request.getUserLastName());
			informationGiver.setUserPhoneNumber(request.getUserPhoneNumber());
			informationGiver.setUserDescription(request.getDescription());
			informationGiver.setDidYouTeach(request.getDidYouTeach());
			informationGiver.setCurrentSchool(request.getCurrentSchool());
			informationGiver.setCurrentLevel(request.getCurrentLevel() );
			informationGiver.setCurrentCourse(request.getCurrentCourse());
			informationGiver.setPreviousOnlineTeaching(request.getPreviousOnlineTeaching());
			informationGiver.setPreviousOnlinePlatforms(request.getPreviousOnlinePlatforms());
			informationGiver.setPreviousSupportCourses(request.getPreviousSupportCourses());
			informationGiver.setSupportCourseLevel(request.getSupportCourseLevel());
			informationGiver.setSupportCourseCourse(request.getSupportCourseCourse());
			//informationGiver.setUserPassword(request.getUserPassword());
			informationGiver.setAccountStatus(AccountStatus.ACTIVE);
			informationGiver.setUserRole(request.getUserRole());
			informationGiver.setLastActivationDate(new Date());
			informationGiver.setValidate(true);
			informationGiver.setExemptFromFees(true);
			informationGiver.setDateValidation(new Date());
			informationGiver.setTermsAccepted(request.isTermsAccepted());
			
		}
		
		return informationGiver;
	}
	

}
