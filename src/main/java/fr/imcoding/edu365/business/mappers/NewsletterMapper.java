package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.client.dtos.response.NewsLetterResponse;
import fr.imcoding.edu365.persistence.entities.SubscribtionUser;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 16/12/2022
 */
@Service
public class NewsletterMapper {

  public NewsLetterResponse toNewsLetterResponse(SubscribtionUser subscribtionUser){
    return new NewsLetterResponse(subscribtionUser.getEmail(),subscribtionUser.getCreatedAt());
  }

}
