package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.*;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.business.services.files.DBFileStorageService;
import fr.imcoding.edu365.business.services.files.FilesStorageService;
import fr.imcoding.edu365.dtos.*;
import fr.imcoding.edu365.enumeration.*;
import fr.imcoding.edu365.exceptions.BadRequestException;
import fr.imcoding.edu365.exceptions.UserForbiddenException;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import fr.imcoding.edu365.persistence.repositories.SkillAreaSectionRepository;
import fr.imcoding.edu365.persistence.repositories.TeacherCourseRepository;
import fr.imcoding.edu365.persistence.specifications.TeacherCourseSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import fr.imcoding.edu365.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeacherCourseService {

  private final TeacherCourseRepository teacherCourseRepository;
  private final SkillAreaSectionRepository skillAreaSectionRepository;
  private final TeacherCourseMapper teacherCourseMapper;

  private final UserService userService;
  private final SkillMapper skillMapper;
  private final SkillAreaMapper skillAreaMapper;
  private final SkillAreaSectionMapper skillAreaSectionMapper;
  private final MediaService mediaService;
  private final FilesStorageService dBFileStorageService;
  private final PackageSubscriptionRepository subscriptionRepository;
  private final LessonCorrectionService lessonCorrectionService;
  private final EmailService emailService;
  private final TraceTeacherCourseService traceTeacherCourseService;
  private final SkillSubscriptionService skillSubscriptionService;


  public void saveTeacherCourse(TeacherCourseRequest teacherCourseRequest) {
    //La premiére étape est de persister les medias
    List<Media> mediaList = new ArrayList<>();
    if (teacherCourseRequest.getFiles() != null && !teacherCourseRequest.getFiles().isEmpty()) {
      teacherCourseRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.TEACHER_COURSE);
          mediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    // La deuxieme etape est de créer les cours avec les medias

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
      teacherCourse.setShouldBeDisplayed(teacherCourseRequest.getShouldBeDisplayed());
      teacherCourse.setSkillArea(skillAreaMapper.toSkillArea(teacherCourseRequest.getSkillArea()));
      //teacherCourse.setSkillAreaSections(teacherCourseRequest.getSkillAreaSections().stream().map(skillAreaSectionRepository::findByCode).collect(Collectors.toList()));
      teacherCourse.setSkillAreaSection(skillAreaSectionMapper.toSkillAreaSection(teacherCourseRequest.getSkillAreaSection()));
      teacherCourse.getMedias().addAll(mediaList);
      teacherCourse =  teacherCourseRepository.save(teacherCourse);
      if(teacherCourse.getShouldBeDisplayed() == true) {
        sendNotificationEmailAfterPublishingNewCourse(teacherCourse);
      }
      //teacherCourse.setSkillAreaSection(skillAreaSectionMapper.toSkillAreaSection(teacherCourseRequest.getSkillAreaSection()));


    /*List<Media> mediaList = new ArrayList<>();

    if (teacherCourseRequest.getFiles() != null && !teacherCourseRequest.getFiles().isEmpty()) {
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
    teacherCourse =  teacherCourseRepository.save(teacherCourse);
    sendNotificationEmailAfterPublishingNewCourse(teacherCourse);
    return teacherCourse;

     */
  }

  public void sendNotificationEmailAfterPublishingNewCourse(TeacherCourse teacherCourse) {

    Map<String, Object> maps = new HashMap<>();
    maps.put("lessonUuid", teacherCourse.getUuid().toString());
    maps.put("title", teacherCourse.getTitle());
    maps.put("skill", teacherCourse.getSkill().getSkillLabel());
    maps.put("skillArea", teacherCourse.getSkillArea().getSkillAreaLabel());
    maps.put("section", teacherCourse.getSkillAreaSection().getLabel());

    // String subject = Constants.MAIL_SUBJECT_USER_PUBLISHED_COURSE + " [ " + maps.get("skill") + " ] ";
    String subject = Constants.MAIL_SUBJECT_USER_PUBLISHED_COURSE + " [ " + maps.get("title") + " ] ";
    
    EmailDto emailDto = new EmailDto(
            subject, "notif-user-pub-course.html", maps,
            new HashMap<>(), EmailContext.NOTIF_USER_PUB_COURSE);

    emailService.sendMail(emailDto,
            userService.getStudentsWithLevel(teacherCourse.getSkillArea().getSkillAreaCode(), teacherCourse.getSkillAreaSection().getCode()).stream().map(InformationSeeker::getUserEmail).collect(Collectors.toList()));
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
        dBFileStorageService.deleteFile(media.getMediaLabel());
      });
    }
  }

  public void updateCourseVisibility(UUID courseId, Boolean shouldBeDisplayed) {
    TeacherCourse courseToUpdate = teacherCourseRepository
        .findByUuid(courseId).orElse(null);
    if(courseToUpdate != null) {
      if((courseToUpdate.getShouldBeDisplayed() == null || courseToUpdate.getShouldBeDisplayed() == false) && shouldBeDisplayed == true) {
        sendNotificationEmailAfterPublishingNewCourse(courseToUpdate);
      }
      courseToUpdate.setShouldBeDisplayed(shouldBeDisplayed);
      if(shouldBeDisplayed) {
        courseToUpdate.setCreatedAt(new Date());
      }
      teacherCourseRepository.save(courseToUpdate);
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

    //courseToUpdate.setSkillAreaSections(teacherCourseRequest.getSkillAreaSections().stream().map(skillAreaSectionRepository::findByCode).collect(Collectors.toList()));
    courseToUpdate.setSkillAreaSection(skillAreaSectionMapper.toSkillAreaSection(teacherCourseRequest.getSkillAreaSection()));
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
        try {
          dBFileStorageService.deleteFile(media.getMediaLabel());
        } catch (Exception e) {
          log.error("Erreur lors de la suppression physique du fichier ", e.getMessage());
        }

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
    if(courseToUpdate.getShouldBeDisplayed() == false && teacherCourseRequest.getShouldBeDisplayed() == true) {
      sendNotificationEmailAfterPublishingNewCourse(courseToUpdate);
    }
    courseToUpdate.setShouldBeDisplayed(teacherCourseRequest.getShouldBeDisplayed());
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

  @Transactional
  public List<TeacherCourseDetails> getLast6Course(){
  return teacherCourseRepository.findTop6ByOrderByCreatedAtDesc().stream().map(teacherCourseMapper::toTeacherCourseDetails).collect(Collectors.toList());
  }

  public TeacherCourseResponse getTeacherCourse(UUID uuid) {
    User user = null;
    try {
      user = userService.getCurrentUser();
    } catch (Exception e) {
      user = null;
    }
    TeacherCourse teacherCourse = getByUuid(uuid);
    traceTeacherCourseService.addTeacherCourseAccessTracability(teacherCourse, user);
    if (teacherCourse.getIsPremium()) {
      InformationSeeker student = (InformationSeeker) userService.getCurrentUser();
      boolean hasSubscription = subscriptionRepository.existsByStudentAndSubscriptionStatus(student,
          PackageStatus.ACTIVE);
      if(!hasSubscription) {
        hasSubscription = skillSubscriptionService.isUserSubscribedToACousreSkill(uuid);
      }
      if (!hasSubscription) {
        return null;
      }
    }
    LessonCorrection lessonCorrection = lessonCorrectionService.getLessonCorrection(uuid);

    return teacherCourseMapper.toTeacherCourseResponse(teacherCourse, lessonCorrection);
  }

  @Transactional
  public PageDto<TeacherCourseDetails> filterCourses( Integer page,
       Integer offset, String skill,CourseType type,Quarter quarter){
	if (page <= 0) {
	  throw new BadRequestException("page Index should be greater or equals than 1");
	}

    InformationSeeker user=(InformationSeeker) userService.getCurrentUser();
    long totalElementsSize = 0l;
    List<TeacherCourse> courses = new ArrayList<>();
    // Si l'utilisateur a une section specifique au niveaud e son niveau d'etude
    if(user.getCurrentLevelSection() != null) {
      TeacherCourseSearchCriteria courseSearchCriteria =
              new TeacherCourseSearchCriteria(user.getCurrentLevel().getSkillAreaCode(),user.getCurrentLevelSection().getCode(), skill, type, quarter, true);
      Pageable pageable = PageRequest.of(page-1 , offset, Sort.by("createdAt").ascending());
      Page<TeacherCourse> teacherCoursePage = null;
      teacherCoursePage =
              teacherCourseRepository.findAll(TeacherCourseSpecification
                      .createAnnouncementSpecifications(courseSearchCriteria), pageable);
      courses = teacherCoursePage.getContent();
      totalElementsSize = teacherCoursePage.getTotalElements();
    } // Si l'utilisateur n'a pas une section specifique au niveaud e son niveau d'etude
    else if(user.getCurrentLevel() != null) {
        TeacherCourseSearchCriteria courseSearchCriteria =
                new TeacherCourseSearchCriteria(user.getCurrentLevel().getSkillAreaCode(),null, skill, type, quarter, true);
      Pageable pageable = PageRequest.of(page-1 , offset, Sort.by("createdAt").ascending());
      Page<TeacherCourse> teacherCoursePage = null;
      teacherCoursePage =
              teacherCourseRepository.findAll(TeacherCourseSpecification
                      .createAnnouncementSpecifications(courseSearchCriteria), pageable);
      totalElementsSize = teacherCoursePage.getTotalElements();
      courses = teacherCoursePage.getContent();
    }

    List<TeacherCourseDetails> courseDetailsList = courses.stream()
        .map(teacherCourseMapper::toTeacherCourseDetailsFilter)
        .collect(Collectors.toList());
    return new PageDto<>(courseDetailsList, totalElementsSize) ;
  }

  public PageDto<TeacherCourseResponse> filterCoursesForAdmin(String skillAreaCode, String sectionCode, String skillLabel,Quarter quarter, CourseType type){

    long totalElementsSize = 0l;
    List<TeacherCourse> courses = new ArrayList<>();
    TeacherCourseSearchCriteria courseSearchCriteria =
        new TeacherCourseSearchCriteria(skillAreaCode,sectionCode, skillLabel, type, quarter, null);
    Pageable pageable = PageRequest.of(0 , 10000, Sort.by("createdAt").ascending());
    Page<TeacherCourse> teacherCoursePage = null;
    teacherCoursePage =
        teacherCourseRepository.findAll(TeacherCourseSpecification
            .createAnnouncementSpecifications(courseSearchCriteria), pageable);
    courses = teacherCoursePage.getContent();
    totalElementsSize = teacherCoursePage.getTotalElements();

    List<TeacherCourseResponse> courseDetailsList = courses.stream()
        .map(teacherCourseMapper::toTeacherCourseResponse)
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
