package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.client.dtos.response.NotSentEmailResponse;
import fr.imcoding.edu365.persistence.entities.NotSentEmailTraceability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 18/11/2022
 */
@Service
@RequiredArgsConstructor
public class NotSentEmailMapper {

  public NotSentEmailResponse toNotSentEmailResponse(NotSentEmailTraceability emailTraceability){
    return new NotSentEmailResponse(emailTraceability.getEmail(),emailTraceability.getContext(),emailTraceability.getAttemptsNumber(),
    emailTraceability.getSubject(),emailTraceability.getTemplateName(),emailTraceability.getExtraInformation());
  }
}
