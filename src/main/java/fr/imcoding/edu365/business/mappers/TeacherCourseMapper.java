package fr.imcoding.edu365.business.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.dtos.CourseCreatorDto;
import fr.imcoding.edu365.dtos.LessonCorrectionResponse;
import fr.imcoding.edu365.dtos.TeacherCourseDetails;
import fr.imcoding.edu365.dtos.TeacherCourseResponse;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */

@Component
@RequiredArgsConstructor
public class TeacherCourseMapper {
  private final SkillMapper skillMapper;
  private final SkillAreaMapper skillAreaMapper;
  private final MediaDatailsMapper mediaMapper;
  private final UserMapper userMapper;
  private final UserService userService;
  private final PackageSubscriptionRepository subscriptionRepository;

  public  TeacherCourseResponse toTeacherCourseResponse(TeacherCourse teacherCourse){
    return TeacherCourseResponse.builder().courseUuid(teacherCourse.getUuid())
        .title(teacherCourse.getTitle())
        .type(teacherCourse.getType())
        .description(teacherCourse.getDescription())
        .creatorEmail(teacherCourse.getCreator()!=null? teacherCourse.getCreator().getUserEmail():null)
        .quarter(teacherCourse.getQuarter())
        .skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
        .skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
        .medias(teacherCourse.getMedias().stream().map(media->mediaMapper.toMediaDetails(media)).collect(
            Collectors.toList())).isPremium(teacherCourse.getIsPremium()).build();
  }
  
	public TeacherCourseResponse toTeacherCourseResponse(TeacherCourse teacherCourse,
			LessonCorrection lessonCorrection) {
		return TeacherCourseResponse.builder().courseUuid(teacherCourse.getUuid()).title(teacherCourse.getTitle())
				.type(teacherCourse.getType()).description(teacherCourse.getDescription())
				.creatorEmail(teacherCourse.getCreator() != null ? teacherCourse.getCreator().getUserEmail() : null)
				.quarter(teacherCourse.getQuarter()).skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
				.skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
				.lessonCorrection(lessonCorrection != null
						? LessonCorrectionResponse.builder().lessonCorrectionUuid(lessonCorrection.getUuid()).build()
						: null)
				.medias(teacherCourse.getMedias().stream().map(media -> mediaMapper.toMediaDetails(media))
						.collect(Collectors.toList()))
				.isPremium(teacherCourse.getIsPremium()).build();
	}

	public TeacherCourseResponse toTeacherCourseResponseWithDetailedCorrection(TeacherCourse teacherCourse,
			LessonCorrection lessonCorrection) {
		return TeacherCourseResponse.builder().courseUuid(teacherCourse.getUuid()).title(teacherCourse.getTitle())
				.type(teacherCourse.getType()).description(teacherCourse.getDescription())
				.courseCreator(teacherCourse.getCreator()!=null?CourseCreatorDto.builder()
			            .userDetails(userMapper.toUserDetails(teacherCourse.getCreator()))
			            .skill(teacherCourse.getCreator() != null ? 
			            		skillMapper.toSkillDto(teacherCourse.getCreator().getSkills().stream().findFirst().orElse(null))
			            		: null)
			            .build():null)
				.quarter(teacherCourse.getQuarter()).skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
				.skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
				.lessonCorrection(lessonCorrection != null ? LessonCorrectionResponse.builder()
						.lessonCorrectionUuid(lessonCorrection.getUuid()).description(lessonCorrection.getDescription())
						.medias(lessonCorrection.getMedias().stream().map(media -> mediaMapper.toMediaDetails(media))
								.collect(Collectors.toList()))
						.onlySubscribedUsers(lessonCorrection.isOnlySubscribedUsers()).build() : null)
				.build();
	}

  public TeacherCourseDetails toTeacherCourseDetails(TeacherCourse teacherCourse){
    return TeacherCourseDetails.builder().title(teacherCourse.getTitle())
        .courseUuid(teacherCourse.getUuid())
        .skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
        .skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
        .courseCreator(teacherCourse.getCreator()!=null?CourseCreatorDto.builder()
            .userDetails(userMapper.toUserDetails(teacherCourse.getCreator()))
            .skill(teacherCourse.getCreator() != null ? 
            		skillMapper.toSkillDto(teacherCourse.getCreator().getSkills().stream().findFirst().orElse(null))
            		: null)
            .build():null)

        .build();
  }

  public TeacherCourseDetails toTeacherCourseDetailsFilter(TeacherCourse teacherCourse){
	  InformationSeeker student = (InformationSeeker) userService.getCurrentUser();
		boolean hasSubscription = subscriptionRepository.existsByStudentAndSubscriptionStatus(student,
				PackageStatus.ACTIVE);
		
    return TeacherCourseDetails.builder().title(teacherCourse.getTitle())
        .courseUuid(teacherCourse.getUuid())
        .skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
        .skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
        .courseCreator(teacherCourse.getCreator()!=null?CourseCreatorDto.builder()
            .userDetails(userMapper.toUserDetail(teacherCourse.getCreator()))
            .skill(teacherCourse.getCreator() != null ?
                skillMapper.toSkillDto(teacherCourse.getCreator().getSkills().stream().findFirst().orElse(null))
                : null)
            .build():null).type(teacherCourse.getType()).quarter(teacherCourse.getQuarter())
        .isPremium(teacherCourse.getIsPremium())
        .canBeOpened(!teacherCourse.getIsPremium() ? true : hasSubscription ? true: false)
        .build();
  }

  public TeacherCourseDetails toLessonDetails(TeacherCourse teacherCourse){
    return TeacherCourseDetails.builder().title(teacherCourse.getTitle())
        .courseUuid(teacherCourse.getUuid()).description(teacherCourse.getDescription())
        .skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
        .skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
        .courseCreator(teacherCourse.getCreator()!=null?CourseCreatorDto.builder()
            .userDetails(userMapper.toUserDetail(teacherCourse.getCreator()))
            .skill(teacherCourse.getCreator() != null ?
                skillMapper.toSkillDto(teacherCourse.getCreator().getSkills().stream().findFirst().orElse(null))
                : null)
            .build():null)
        .type(teacherCourse.getType())
        .quarter(teacherCourse.getQuarter())
        .medias(teacherCourse.getMedias().stream().map(media->mediaMapper.toMediaDetails(media)).collect(
            Collectors.toList()))
        .isPremium(teacherCourse.getIsPremium())

        .build();
  }

  public TeacherCourseDetails toSimilarLessonDetails(TeacherCourse teacherCourse){
    return TeacherCourseDetails.builder().title(teacherCourse.getTitle())
        .courseUuid(teacherCourse.getUuid())
        .build();
  }

  public TeacherCourseDetails toLessonCorrectionDetails(TeacherCourse teacherCourse,LessonCorrectionResponse lessonCorrection){
    return TeacherCourseDetails.builder().title(teacherCourse.getTitle())
        .courseUuid(teacherCourse.getUuid()).lessonCorrection(lessonCorrection!=null?lessonCorrection:null)
        .build();
  }



  public TeacherCourseDetails toLessonDetails(TeacherCourse teacherCourse,LessonCorrectionResponse lessonCorrection){
    return TeacherCourseDetails.builder().title(teacherCourse.getTitle())
        .courseUuid(teacherCourse.getUuid()).description(teacherCourse.getDescription())
        .skill(skillMapper.toSkillDto(teacherCourse.getSkill()))
        .skillArea(skillAreaMapper.toSkillAreaDto(teacherCourse.getSkillArea()))
        .courseCreator(teacherCourse.getCreator()!=null?CourseCreatorDto.builder()
            .userDetails(userMapper.toUserDetail(teacherCourse.getCreator()))
            .skill(teacherCourse.getCreator() != null ?
                skillMapper.toSkillDto(teacherCourse.getCreator().getSkills().stream().findFirst().orElse(null))
                : null)
            .build():null)
        .type(teacherCourse.getType())
        .quarter(teacherCourse.getQuarter())
        .medias(teacherCourse.getMedias().stream().map(media->mediaMapper.toMediaDetails(media)).collect(
            Collectors.toList()))
        .lessonCorrection(lessonCorrection!=null?lessonCorrection:null)

        .build();
  }

}
