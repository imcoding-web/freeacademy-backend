package fr.imcoding.edu365.business.services;

import java.util.UUID;

import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.mappers.TeacherCourceCorrectionMapper;
import fr.imcoding.edu365.business.mappers.TeacherCourseMapper;
import fr.imcoding.edu365.dtos.TeacherCourseDetails;
import fr.imcoding.edu365.dtos.TeacherCourseResponse;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 16/10/2023
 */

@RequiredArgsConstructor
@Service
public class TeacherCourseCorrectionService {
  private final TeacherCourceCorrectionMapper teacherCourceCorrectionMapper;
  private final LessonCorrectionService lessonCorrectionService;
  private final TeacherCourseService teacherCourseService;
  private final TeacherCourseMapper teacherCourseMapper;
  private final UserService userService;
  private final PackageSubscriptionRepository subscriptionRepository;



  public TeacherCourseDetails getDetailsByUuid(UUID uuid){
	   LessonCorrection lessonCorrection=lessonCorrectionService.getLessonCorrectionByUuid(uuid);
    return teacherCourceCorrectionMapper.toTeacherCourseDetails(teacherCourseService.getByUuid(uuid),lessonCorrection);
  }

	public TeacherCourseResponse getLessonCorrectionDetail(UUID lessonUuid) {

		if(userService.getCurrentUser().getUserRole().getRoleCode() == RoleCode.INFORMATION_SEEKER) {
			//pour l'etudiant on passe l'uuid de la correction et pas du cours
			LessonCorrection lessonCorrection = lessonCorrectionService.getLessonCorrectionByUuid(lessonUuid);

			InformationSeeker student = (InformationSeeker) userService.getCurrentUser();
			boolean hasSubscription = subscriptionRepository.existsByStudentAndSubscriptionStatus(student,
					PackageStatus.ACTIVE);
			if (!hasSubscription && lessonCorrection.isOnlySubscribedUsers())
				return null;
			return teacherCourseMapper.toTeacherCourseResponseWithDetailedCorrection(lessonCorrection.getCourse(),
					lessonCorrection);
		} else {
			TeacherCourse course = teacherCourseService.getByUuid(lessonUuid);
			LessonCorrection lessonCorrection = lessonCorrectionService.getLessonCorrection(lessonUuid);
			/*if (lessonCorrection == null)
				return null;*/
			return teacherCourseMapper.toTeacherCourseResponseWithDetailedCorrection(course,
					lessonCorrection);

		}


	}
}
