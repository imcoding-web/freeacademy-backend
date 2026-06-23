package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.LessonCorrectionService;
import fr.imcoding.edu365.dtos.LessonCorrectionResponse;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@Component
@RequiredArgsConstructor
public class LessonCorrectionMapper {
  private final MediaDatailsMapper mediaMapper;


  public LessonCorrectionResponse toLessonCorrectionResponse(LessonCorrection lessonCorrection){
    return lessonCorrection!=null? LessonCorrectionResponse.builder().lessonCorrectionUuid(lessonCorrection.getUuid())
        .description(lessonCorrection.getDescription())
       .onlySubscribedUsers(lessonCorrection.isOnlySubscribedUsers())
        .medias(lessonCorrection.getMedias().stream().map(media->mediaMapper.toMediaDetails(media)).collect(
            Collectors.toList())).build():null;
  }

}
