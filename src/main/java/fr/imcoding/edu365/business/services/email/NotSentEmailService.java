package fr.imcoding.edu365.business.services.email;

import fr.imcoding.edu365.dtos.EmailTraceabilityDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.NotSentEmailTraceability;
import fr.imcoding.edu365.persistence.repositories.NotSentEmailTraceabilityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 07/11/2022
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotSentEmailService {
  private final NotSentEmailTraceabilityRepository emailTraceabilityRepository;


  public List<NotSentEmailTraceability> getAllEmails(){
    return  emailTraceabilityRepository.findAll();
  }

  public NotSentEmailTraceability saveEmailTraceability(EmailTraceabilityDto emailTraceability) {
    NotSentEmailTraceability sentEmail=new NotSentEmailTraceability();
    sentEmail.setContext(emailTraceability.getContext());
    sentEmail.setExtraInformation(emailTraceability.getExtraInformation());
    sentEmail.setSubject(emailTraceability.getSubject());
    sentEmail.setTemplateName(emailTraceability.getTemplateName());
    sentEmail.setEmail(emailTraceability.getEmail());
    return this.emailTraceabilityRepository.save(sentEmail);
  }

  public void deleteEmailTaceability(NotSentEmailTraceability emailTraceability ) {
     this.emailTraceabilityRepository.delete(emailTraceability);
  }

  public NotSentEmailTraceability updateAttemptNumber(NotSentEmailTraceability emailTraceability ) {
    NotSentEmailTraceability notSentEmailTraceability= emailTraceabilityRepository.findByUuid(emailTraceability.getUuid()).orElse(null);
    notSentEmailTraceability.setAttemptsNumber(notSentEmailTraceability.getAttemptsNumber()+1);
    return this.emailTraceabilityRepository.save(notSentEmailTraceability);
  }

  public NotSentEmailTraceability getEmailTraceability(String email,EmailContext emailContext) {
     return this.emailTraceabilityRepository.findByEmailAndContext(email,emailContext).orElse(null);
  }


}
