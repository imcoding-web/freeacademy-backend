package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.ContactMessageService;
import fr.imcoding.edu365.dtos.MessageDto;
import fr.imcoding.edu365.persistence.entities.ContactMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 05/12/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/contact-message")
@RequiredArgsConstructor
public class ContactMessageController {

  private final ContactMessageService contactMessageService;

  @PostMapping()
  public ContactMessage saveAnnouncement(@RequestBody MessageDto messageDto) {
    return this.contactMessageService.saveContactMessage(messageDto);
  }

}
