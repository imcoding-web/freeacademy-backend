package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 23/06/2022
 */
@Service
@RequiredArgsConstructor
public class MediaDatailsMapper {
  private final MediaRepository mediaRepository;

  public MediaDetails toMediaDetails(Media media) {
     if(media!=null){
       return new MediaDetails(media.getUuid(), media.getMediaUrl(),media.getMediaLabel(),media.getOriginalName());
     }else return null;
  }
  public Media toMedia(MediaDetails media) {
    return mediaRepository.findByUuid(media.getMediaUuid());
  }

}
