package fr.imcoding.edu365.sceduled;

import fr.imcoding.edu365.business.services.AnnouncementService;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.business.services.email.NotSentEmailService;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.NotSentEmailTraceability;
import fr.imcoding.edu365.persistence.repositories.NotSentEmailTraceabilityRepository;
import fr.imcoding.edu365.utils.Constants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 07/11/2022
 */
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class ResendMailTask {
  @Value("${edu365.email.max-attempt}")
  private int attemptNumber;

  private final NotSentEmailService notSentEmailService;
  private final EmailService emailService;




  @Scheduled(cron = "${edu365.email.scheduler.automatic.sent.cron}")
  public void resendMail() {
    log.info("***START AUTOMATIC EMAIL SENT PROCESS ***");

    notSentEmailService.getAllEmails().stream().forEach(email -> {
      if( email.getAttemptsNumber()< attemptNumber) {
        Map<String, Object> maps = new HashMap<>();
        maps.put(Constants.CODE, email.getExtraInformation());
        EmailDto userEmailDto = new EmailDto(Constants.MAIL_SUBJECT_USER_WELCOME,"welcome-expert.html", maps, new HashMap<>(),EmailContext.WELCOME_USER);
        emailService.sendMail(userEmailDto,Arrays.asList(email.getEmail()));
      }else{
       // notSentEmailService.deleteEmailTaceability(email);
      }
    });
    log.info("***FINISH AUTOMATIC EMAIL SENT PROCESS ***");

  }



}
