package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.MessageDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.ContactMessage;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.ContactMessageRepository;
import fr.imcoding.edu365.utils.Constants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 05/12/2022
 */
@Service
@RequiredArgsConstructor
public class ContactMessageService {

  private final ContactMessageRepository contactMessageRepository;
  private final UserService userService;
  private final EmailService emailService;


  public ContactMessage saveContactMessage(MessageDto messageRequest) {

    User user=userService.getCurrentUser();
    ContactMessage message=new ContactMessage();
    message.setMessage(messageRequest.getBody());
    message.setSubject(messageRequest.getSubject());
    message.setSender(user);
    message=contactMessageRepository.save(message);
    List<String> destination = (userService.getUserDetailsyRole(RoleCode.ADMINISTRATOR).stream().map(User::getUserEmail).collect(
        Collectors.toList()));
    Map<String, Object> maps = new HashMap<>();
    maps.put("subject", message.getSubject());
    maps.put("body", message.getMessage());
    maps.put("fullname", user.getUserLastName()+" "+user.getUserFirstName());
    maps.put("phoneNumber", user.getUserPhoneNumber());
    maps.put("email", user.getUserEmail());
    maps.put("type", (user.getUserRole().getRoleCode()==(RoleCode.INFORMATION_GIVER))? "Eléve" :"Enseignant");
    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_NOTIF_ADMIN_NEW_CONTACT_MESSAGE, "notif-admin-new-contact-message.html", maps,
            new HashMap<>(),EmailContext.NOTIF_ADMIN_NEW_CONTACT_MESSAGE);

    emailService.sendMail(emailDto, destination);
    return message;
  }

}
