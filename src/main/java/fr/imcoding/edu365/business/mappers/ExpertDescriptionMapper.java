package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.ExpertUtils;
import fr.imcoding.edu365.business.services.UserUtils;
import fr.imcoding.edu365.dtos.ExpertDescription;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 27/06/2022
 */
@Service
@RequiredArgsConstructor
public class ExpertDescriptionMapper {
  private final UserUtils userUtils;
  private final ExpertUtils expertUtils;
  public ExpertDescription toExpertDescription(InformationGiver user) {
    return new ExpertDescription(user.getUserDescription(),userUtils.getPictureCover(user),expertUtils.getVideo(user),expertUtils.getVideoPresentation(user));
  }
}
