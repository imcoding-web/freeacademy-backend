package fr.imcoding.edu365.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.imcoding.edu365.business.services.files.FilesStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.imcoding.edu365.business.mappers.LessonCorrectionMapper;
import fr.imcoding.edu365.business.services.files.DBFileStorageService;
import fr.imcoding.edu365.dtos.LessonCorrectionRequest;
import fr.imcoding.edu365.dtos.LessonCorrectionResponse;
import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.repositories.LessonCorrectionRepository;
import fr.imcoding.edu365.persistence.repositories.TeacherCourseRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@Service
@RequiredArgsConstructor
public class LessonCorrectionService {
  private final LessonCorrectionRepository lessonCorrectionRepository;
  private final LessonCorrectionMapper lessonCorrectionMapper;
  private final MediaService mediaService;
  private final TeacherCourseRepository teacherCourseRepository;
  private final FilesStorageService dBFileStorageService;
  //private final TeacherCourceCorrectionMapper teacherCourceCorrectionMapper;

  public LessonCorrection saveLessonCorrection(LessonCorrectionRequest lessonCorrectionRequest) {
    LessonCorrection lessonCorrection = new LessonCorrection();
    lessonCorrection.setDescription(lessonCorrectionRequest.getDescription());
    TeacherCourse teacherCourse=teacherCourseRepository.findByUuid(lessonCorrectionRequest.getLessonUuid()).orElse(null);
    lessonCorrection.setCourse(teacherCourse);
    lessonCorrection.setOnlySubscribedUsers(lessonCorrectionRequest.isOnlySubscribedUsers());
    List<Media> mediaList = new ArrayList<>();
    if (lessonCorrectionRequest.getFiles() != null && lessonCorrectionRequest.getFiles().size() > 0) {
      lessonCorrectionRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.TEACHER_COURSE);
          mediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      lessonCorrection.getMedias().addAll(mediaList);
    }
    return lessonCorrectionRepository.save(lessonCorrection);
  }

	public LessonCorrection getLessonCorrection(UUID lessonUuid) {
		return lessonCorrectionRepository.findByCourseUuid(lessonUuid).orElse(null);
	}

	public LessonCorrection getLessonCorrectionByUuid(UUID lessonUuid) {
		return lessonCorrectionRepository.findByUuid(lessonUuid).orElse(null);
	}

  public LessonCorrectionResponse getLessonCorrectionDetails(UUID lessonUuid) {
    return lessonCorrectionMapper.toLessonCorrectionResponse(getLessonCorrection(lessonUuid));
  }

  @Transactional
  public LessonCorrection updateLessonCorrection(LessonCorrectionRequest lessonCorrectionRequest) {
    List<Media> mediaListToDelete = checkMediaTeacherCourse(lessonCorrectionRequest.getLessonCorrectionUuid(),
        lessonCorrectionRequest.getMedias());
    List<Media> newMediaList = new ArrayList<>();
    LessonCorrection lessonCorrection = lessonCorrectionRepository
        .findByUuid(lessonCorrectionRequest.getLessonCorrectionUuid()).orElse(null);

    lessonCorrection.setDescription(lessonCorrectionRequest.getDescription());
    lessonCorrection.setOnlySubscribedUsers(lessonCorrectionRequest.isOnlySubscribedUsers());

    if (!mediaListToDelete.isEmpty()) {
      mediaListToDelete.forEach(media -> {
        mediaService.deleteMedia(media.getId());
        dBFileStorageService.deleteFile(media.getMediaLabel());
      });

      lessonCorrection.getMedias().removeAll(mediaListToDelete);

    }
    if (lessonCorrectionRequest.getFiles() != null && lessonCorrectionRequest.getFiles().size() > 0) {
      lessonCorrectionRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.PICTURE_PROJECT);
          newMediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      lessonCorrection.getMedias().addAll(newMediaList);
    }
    LessonCorrection correction = lessonCorrectionRepository.save(lessonCorrection);

    return correction;
  }

  public List<Media> checkMediaTeacherCourse(UUID lessonCorrectionUuid, List<MediaDto> newMediaList) {
    LessonCorrection lessonCorrection = lessonCorrectionRepository.findByUuid(lessonCorrectionUuid).orElse(null);
    List<Media> mediaList = new ArrayList<>();

    if (newMediaList != null && !newMediaList.isEmpty() && lessonCorrection != null) {
      mediaList = lessonCorrection.getMedias()
          .stream()
          .filter(media -> newMediaList.stream()
              .noneMatch(dto -> dto.getMediaUuid().equals(media.getUuid())))
          .collect(Collectors.toList());
    }

    return mediaList;
  }
}
