package fr.imcoding.edu365.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.business.mappers.SkillMapper;
import fr.imcoding.edu365.business.mappers.TeacherCourseMapper;
import fr.imcoding.edu365.business.services.files.IFileService;
import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.dtos.TeacherCourseDetails;
import fr.imcoding.edu365.dtos.TeacherCourseRequest;
import fr.imcoding.edu365.dtos.TeacherCourseResponse;
import fr.imcoding.edu365.dtos.TeacherCourseSearchCriteria;
import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.Quarter;
import fr.imcoding.edu365.exceptions.BadRequestException;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import fr.imcoding.edu365.persistence.repositories.TeacherCourseRepository;
import fr.imcoding.edu365.persistence.specifications.TeacherCourseSpecification;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Service
@RequiredArgsConstructor
public class TeacherCourseService {

  private final TeacherCourseRepository teacherCourseRepository;
  private final TeacherCourseMapper teacherCourseMapper;

  private final UserService userService;
  private final SkillMapper skillMapper;
  private final SkillAreaMapper skillAreaMapper;
  private final MediaService mediaService;
  private final IFileService dBFileStorageService;
  private final PackageSubscriptionRepository subscriptionRepository;
  private final LessonCorrectionService lessonCorrectionService;

  public TeacherCourse saveTeacherCourse(TeacherCourseRequest teacherCourseRequest) {
    TeacherCourse teacherCourse = new TeacherCourse();

    if (!teacherCourseRequest.getCreatorEmail().isEmpty()) {
      InformationGiver creator = (InformationGiver) userService
          .getByUserEmail(teacherCourseRequest.getCreatorEmail());
      if (creator != null) {
        teacherCourse.setCreator(creator);
      }
    }

    teacherCourse.setTitle(teacherCourseRequest.getTitle());
    teacherCourse.setDescription(teacherCourseRequest.getDescription());
    teacherCourse.setType(teacherCourseRequest.getType());
    teacherCourse.setQuarter(teacherCourseRequest.getQuarter());
    teacherCourse.setSkill(skillMapper.toSkill(teacherCourseRequest.getSkill()));
    teacherCourse.setIsPremium(teacherCourseRequest.getIsPremium());

    teacherCourse.setSkillArea(skillAreaMapper.toSkillArea(teacherCourseRequest.getSkillArea()));
    List<Media> mediaList = new ArrayList<>();

    if (teacherCourseRequest.getFiles() != null && teacherCourseRequest.getFiles().size() > 0) {
      teacherCourseRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.TEACHER_COURSE);
          mediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      teacherCourse.getMedias().addAll(mediaList);
    }
    return teacherCourseRepository.save(teacherCourse);
  }


  public List<TeacherCourseResponse> getTeacherCourse() {
    return teacherCourseRepository.findAll().stream()
        .map(teacherCourse -> teacherCourseMapper.toTeacherCourseResponse(teacherCourse))
        .collect(Collectors.toList());
  }

  public TeacherCourseResponse getTeacherCourseDetails(UUID uuid) {
    return teacherCourseMapper
        .toTeacherCourseResponse(teacherCourseRepository.findByUuid(uuid).orElse(null));
  }


  public TeacherCourse getByUuid(UUID uuid) {
    return teacherCourseRepository.findByUuid(uuid).orElse(null);
  }

  @Transactional
  public void deleteTeacherCourse(UUID projectUuid) {
    TeacherCourse teacherCourseTodelete = getByUuid(projectUuid);
    if (teacherCourseTodelete != null) {
      List<Media> teacherCourseMedia = teacherCourseTodelete.getMedias();
      teacherCourseRepository.delete(teacherCourseTodelete);
      // remove media from disk
      teacherCourseMedia.stream().forEach(media -> {
        dBFileStorageService.deleteFile(media);
      });
    }
  }

  @Transactional
  public TeacherCourse updateTeacherCourse(TeacherCourseRequest teacherCourseRequest) {
    List<Media> mediaListToDelete = checkMediaTeacherCourse(teacherCourseRequest.getCourseUuid(),
        teacherCourseRequest.getMedias());
    List<Media> newMediaList = new ArrayList<>();
    TeacherCourse courseToUpdate = teacherCourseRepository
        .findByUuid(teacherCourseRequest.getCourseUuid()).orElse(null);
    courseToUpdate.setTitle(teacherCourseRequest.getTitle());
    courseToUpdate.setType(teacherCourseRequest.getType());
    courseToUpdate.setDescription(teacherCourseRequest.getDescription());
    courseToUpdate.setSkill(skillMapper.toSkill(teacherCourseRequest.getSkill()));
    courseToUpdate.setSkillArea(skillAreaMapper.toSkillArea(teacherCourseRequest.getSkillArea()));
    courseToUpdate.setIsPremium(teacherCourseRequest.getIsPremium());
    if (!teacherCourseRequest.getCreatorEmail().isEmpty()) {
      InformationGiver creator = (InformationGiver) userService
          .getByUserEmail(teacherCourseRequest.getCreatorEmail());
      if (creator != null) {
        courseToUpdate.setCreator(creator);
      }
    }
    if (!mediaListToDelete.isEmpty()) {
      mediaListToDelete.forEach(media -> {
        mediaService.deleteMedia(media.getId());
        dBFileStorageService.deleteFile(media);
      });

      courseToUpdate.getMedias().removeAll(mediaListToDelete);

    }
    if (teacherCourseRequest.getFiles() != null && teacherCourseRequest.getFiles().size() > 0) {
      teacherCourseRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.PICTURE_PROJECT);
          newMediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      courseToUpdate.getMedias().addAll(newMediaList);
    }
    TeacherCourse project = teacherCourseRepository.save(courseToUpdate);

    return project;
  }

  public List<Media> checkMediaTeacherCourse(UUID projectUuid, List<MediaDto> newMediaList) {
    TeacherCourse teacherCourseToUpdate = getByUuid(projectUuid);
    List<Media> mediaList = new ArrayList<>();

    if (newMediaList != null && !newMediaList.isEmpty() && teacherCourseToUpdate != null) {
      mediaList = teacherCourseToUpdate.getMedias()
          .stream()
          .filter(media -> newMediaList.stream()
              .noneMatch(dto -> dto.getMediaUuid().equals(media.getUuid())))
          .collect(Collectors.toList());
    }

    return mediaList;
  }

  public List<TeacherCourseDetails> getLast6Course(){
  return teacherCourseRepository.findTop6ByOrderByCreatedAtDesc().stream().map(teacherCourse -> teacherCourseMapper.toTeacherCourseDetails(teacherCourse)).collect(Collectors.toList());
  }
  
	public TeacherCourseResponse getTeacherCourse(UUID uuid) {
		TeacherCourse teacherCourse = getByUuid(uuid);
		if (teacherCourse.getIsPremium()) {
			InformationSeeker student = (InformationSeeker) userService.getCurrentUser();
			boolean hasSubscription = subscriptionRepository.existsByStudentAndSubscriptionStatus(student,
					PackageStatus.ACTIVE);
			if (!hasSubscription)
				return null;
		}
		LessonCorrection lessonCorrection=lessonCorrectionService.getLessonCorrection(uuid);

		return teacherCourseMapper.toTeacherCourseResponse(teacherCourse, lessonCorrection);
	}

  public PageDto<TeacherCourseDetails> filterCourses( Integer page,
       Integer offset, String skill,CourseType type,Quarter quarter){
	if (page <= 0) {
	  throw new BadRequestException("page Index should be greater or equals than 1");
	}

    InformationSeeker user=(InformationSeeker) userService.getCurrentUser();
    long totalElementsSize = 0l;
    List<TeacherCourse> courses = new ArrayList<>();
    if(user.getCurrentLevel() != null) {
        TeacherCourseSearchCriteria courseSearchCriteria =
                new TeacherCourseSearchCriteria(user.getCurrentLevel().getSkillAreaCode(), skill, type, quarter);
            
            if (courseSearchCriteria.getSkill()==null && courseSearchCriteria.getType()==null&&
               courseSearchCriteria.getQuarter()==null) {
              Pageable pageable = PageRequest.of(page-1 , offset, Sort.by("createdAt").ascending());
              Page<TeacherCourse> teacherCoursePage =
                  teacherCourseRepository.findAll(TeacherCourseSpecification
                      .createAnnouncementSpecifications(courseSearchCriteria), pageable);

              totalElementsSize = teacherCoursePage.getTotalElements();
              courses = teacherCoursePage.getContent();
            } else {
              courses =
                  teacherCourseRepository.findAll(
                      TeacherCourseSpecification
                          .createAnnouncementSpecifications(courseSearchCriteria));

              totalElementsSize = courses.size();
            }
    }

    List<TeacherCourseDetails> courseDetailsList = courses.stream()
        .map(teacherCourseMapper::toTeacherCourseDetailsFilter)
        .collect(Collectors.toList());
    return new PageDto<>(courseDetailsList, totalElementsSize) ;
  }

 public TeacherCourseDetails getDetailsByUuid(UUID uuid) {
    return teacherCourseMapper
        .toLessonDetails(teacherCourseRepository.findByUuid(uuid).orElse(null));
  }

  public List<TeacherCourseDetails> getSimilarLessons(UUID uuid) {
    TeacherCourse course=getByUuid(uuid);
    return teacherCourseRepository.findTop6BySkillAreaSkillAreaCodeAndSkillSkillCodeOrderByCreatedAtDesc(course.getSkillArea().getSkillAreaCode(),course.getSkill().getSkillCode()).stream()
        .map(teacherCourseMapper::toSimilarLessonDetails)
        .collect(Collectors.toList()).stream().filter(lesson -> !lesson.getCourseUuid().equals(uuid))
        .collect(Collectors.toList());
  }


  }
