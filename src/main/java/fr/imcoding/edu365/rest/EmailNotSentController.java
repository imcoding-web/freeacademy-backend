package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.EmailNotSentService;
import fr.imcoding.edu365.client.dtos.response.NotSentEmailResponse;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.repositories.NotSentEmailTraceabilityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 18/11/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailNotSentController {
  private final EmailNotSentService emailNotSentService;
  @GetMapping()
  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  public PageDto<NotSentEmailResponse> getAllNotSentEmails(
      @RequestParam(value = "email-context", required = false) EmailContext emailContext,
      @RequestParam(name = "page") Integer page,
      @RequestParam(name = "offset") Integer offset

  ) {
    return emailNotSentService.getAllNotSentEmails(emailContext,page,offset);
  }

  @PostMapping
  public void resendEmail(@RequestBody NotSentEmailResponse notSentEmailResponse){
    emailNotSentService.resendMail(notSentEmailResponse);
  }

}
