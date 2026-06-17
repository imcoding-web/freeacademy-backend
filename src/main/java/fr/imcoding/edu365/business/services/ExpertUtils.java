package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.MediaDatailsMapper;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 23/06/2022
 */
@Service
@RequiredArgsConstructor
public class ExpertUtils {
  private final MediaDatailsMapper mediaDatailsMapper;

 public MediaDetails getGraduation(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
        .filter(media -> media.getMediaContext() == MediaContext.PICTURE_GRADUATION)
        .findFirst().orElse(null)) :null;

  }
  public MediaDetails getCerteficate(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
        .filter(media -> media.getMediaContext() == MediaContext.PICTURE_CERTIFICATE)
        .findFirst().orElse(null)):null;

  }
  public MediaDetails getCV(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
        .filter(media -> media.getMediaContext() == MediaContext.CV_DOCUMENT)
        .findFirst().orElse(null)):null;

  }
  public MediaDetails getPictureCIN(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
        .filter(media -> media.getMediaContext() == MediaContext.PICTURE_IDENTITY)
        .findFirst().orElse(null)):null;

  }

  public MediaDetails getVideo(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
            .filter(media -> media.getMediaContext() == MediaContext.VIDEO)
            .findFirst().orElse(null)):null;

  }
  public MediaDetails getVideoPresentation(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
            .filter(media -> media.getMediaContext() == MediaContext.VIDEO_PRESENTATION)
            .findFirst().orElse(null)):null;

  }
  public MediaDetails getCoverPicture(InformationGiver informationGiver){
    return informationGiver.getMedias()!= null ? mediaDatailsMapper
        .toMediaDetails(informationGiver.getMedias().stream()
            .filter(media -> media.getMediaContext() == MediaContext.PICTURE_COVER)
            .findFirst().orElse(null)):null;

  }

}
