package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.NotSentEmailMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.response.NotSentEmailResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.NotSentEmailTraceability;
import fr.imcoding.edu365.persistence.repositories.NotSentEmailTraceabilityRepository;
import fr.imcoding.edu365.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 18/11/2022
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotSentService {

  private final NotSentEmailTraceabilityRepository emailTraceabilityRepository;
  private final NotSentEmailMapper notSentEmailMapper;
  private final EmailService emailService;



  public  PageDto<NotSentEmailResponse> getAllNotSentEmails(EmailContext emailContext,Integer page,Integer offset){

    int pageindex = page <= 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(pageindex, offset, Sort.by("createdAt").descending());
    Page<NotSentEmailTraceability> data;
    List<NotSentEmailResponse> result = new ArrayList<>();
    if (emailContext != null)
      data = emailTraceabilityRepository.findByContext(emailContext,pageable);
    else
      data = emailTraceabilityRepository.findAll(pageable);
    if(data.hasContent())
      data.stream().forEach( email ->{
        result.add(notSentEmailMapper.toNotSentEmailResponse(email));
      });
    return new PageDto<>(result, Long.valueOf(result.size()));
  }

  @Transactional
  public void resendMail(NotSentEmailResponse notSentEmailResponse){
    try {
      NotSentEmailTraceability notSentEmailTraceability= emailTraceabilityRepository.findByEmailAndContext(notSentEmailResponse.getEmail(),notSentEmailResponse.getContext()).orElse(null);

      if (notSentEmailTraceability != null) {

        Map<String, Object> maps = new HashMap<>();
        maps.put(Constants.CODE, notSentEmailResponse.getExtraInformation());
        EmailDto userEmailDto = new EmailDto(Constants.MAIL_SUBJECT_USER_WELCOME,
            "welcome-expert.html", maps, new HashMap<>(), notSentEmailResponse.getContext());
        emailService.sendMail(userEmailDto, Arrays.asList(notSentEmailResponse.getEmail()));
        emailTraceabilityRepository.delete(notSentEmailTraceability);
      }
    }catch (Exception e){
       e.printStackTrace();
       log.error("Exception when send Email: {}", e.getMessage());
     }
   }
  }


