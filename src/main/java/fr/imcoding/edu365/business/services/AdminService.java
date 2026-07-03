package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.UserMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.MessageDto;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.Administrator;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.AdminRepository;
import fr.imcoding.edu365.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final AdminRepository adminRepository;
  private final UserService userService;
  private final EmailService emailService;
  private final InformationGiverService informationGiverService;
  private final VerifiedExpertService verifiedExpertService;


  private final UserMapper userMapper;
  public List<Administrator> getAllAdmins() {
    return this.adminRepository.findAll();

  }

  public Administrator saveAdmin(Administrator admin) {
    return this.adminRepository.save(admin);
  }

  public UserDto getUserInfo() {
    User currentUser =  userService.getCurrentUser();
    return (currentUser != null ? userMapper.toUserDto(currentUser) : null);
  }
  
  public void sendEmail(UUID expertUuid,MessageDto messageRequest,EmailContext emailContext) {
	  if(expertUuid == null)
		  this.sendMailToExperts(messageRequest, emailContext);
	  else
		  this.sendMailToExpert(expertUuid, messageRequest, emailContext);
  }

  private void sendMailToExpert(UUID expertUuid,MessageDto messageRequest,EmailContext emailContext) {
    InformationGiver currentUser = (InformationGiver) userService.getUserByUUID(expertUuid);
    List<String> destination=new ArrayList<>();
    if(emailContext.equals(EmailContext.NOTIF_EXPERT_MESSAGE_ADMIN)){
      destination= Arrays.asList(currentUser.getUserEmail());
    }
    destination.stream().forEach(System.out::println);
    Map<String, Object> maps = new HashMap<>();
    maps.put("subject", messageRequest.getSubject());
    maps.put("body", messageRequest.getBody());
    maps.put("fullName", currentUser.getUserFirstName() + " " + currentUser.getUserLastName());

    EmailDto emailDto =
        new EmailDto(
            messageRequest.getSubject(), "notif-expert-message-admin.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_MESSAGE_ADMIN);

    emailService.sendMail(emailDto, destination);
  }
  
  private void sendMailToExperts(MessageDto messageRequest,EmailContext emailContext) {
	    List<String> destination=new ArrayList<>();
	   if(emailContext.equals(EmailContext.NOTIF_EXPERT_ACTIVE_BUT_NOT_VALIDATED_MESSAGE)){
	      List<InformationGiver> experts=informationGiverService.getUserByAccountStatus(AccountStatus.ACTIVE);
	      experts= experts.stream().filter(expert->{ return !verifiedExpertService.checkCompletedValidation(expert.getUuid());}).collect(Collectors
	          .toList());
	      destination = experts.stream().map(InformationGiver::getUserEmail).collect(Collectors.toList());
	    }else if(emailContext.equals(EmailContext.NOTIF_EXPERT_ACTIVE_AND_VALIDATED_MESSAGE)){
      List<InformationGiver> experts=informationGiverService.getUserByAccountStatus(AccountStatus.ACTIVE);
      experts= experts.stream().filter(expert->{ return verifiedExpertService.checkCompletedValidation(expert.getUuid());}).collect(Collectors
          .toList());
      destination = experts.stream().map(InformationGiver::getUserEmail).collect(Collectors.toList());
    }
     else if(emailContext.equals(EmailContext.NOTIF_EXPERT_NOT_ACTIVE_BUT_NOT_DEACTIVATED_MESSAGE)){
       List<InformationGiver> experts=informationGiverService.getUserByAccountStatus(AccountStatus.PENDING);
       destination = experts.stream().map(InformationGiver::getUserEmail).collect(Collectors.toList());
     }
	    destination.stream().forEach(System.out::println);
	    Map<String, Object> maps = new HashMap<>();
	    maps.put("subject", messageRequest.getSubject());
	    maps.put("body", messageRequest.getBody());

	    EmailDto emailDto =
	        new EmailDto(
	            messageRequest.getSubject(), "notif-expert-message-admin.html", maps,
	            new HashMap<>(),EmailContext.NOTIF_EXPERT_MESSAGE_ADMIN);

	    emailService.sendMail(emailDto, destination);
	  }
}
