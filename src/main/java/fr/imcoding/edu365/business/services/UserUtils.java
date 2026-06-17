package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.MediaDatailsMapper;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 23/06/2022
 */
@Service
@RequiredArgsConstructor
public class UserUtils {
private final MediaDatailsMapper mediaDatailsMapper;

 public MediaDetails getPictureProfile(User user){
   return  mediaDatailsMapper.toMediaDetails(user.getMedias().stream()
       .filter(media -> media.getMediaContext() == MediaContext.PICTURE_PROFIL)
       .findFirst().orElse(null));

 }

  public MediaDetails getPictureCover(User user){
    return  mediaDatailsMapper.toMediaDetails(user.getMedias().stream()
        .filter(media -> media.getMediaContext() == MediaContext.PICTURE_COVER)
        .findFirst().orElse(null));

  }





}
