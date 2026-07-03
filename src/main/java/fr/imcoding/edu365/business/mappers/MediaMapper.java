package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 06/06/2022
 */

@Component
@RequiredArgsConstructor
public class MediaMapper {

  private final MediaRepository mediaRepository;

  public Media toMedia(MediaDto media) {
    return mediaRepository.findByUuid(media.getMediaUuid());
  }

	public MediaDto toMediaDto(Media media) {
    return new MediaDto(media.getUuid(), media.getMediaUrl(),media.getMediaContext());
  }

}
