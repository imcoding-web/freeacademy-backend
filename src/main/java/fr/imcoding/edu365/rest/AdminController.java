package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.AdminService;
import fr.imcoding.edu365.client.dtos.response.UserResponse;
import fr.imcoding.edu365.dtos.MessageDto;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 22/11/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
private final AdminService adminService;


  @GetMapping("/admin-info")
  public UserDto getUserInfo() {
    return adminService.getUserInfo();
  }

  @PostMapping("/send-mail")
  public void sendMail(@RequestParam(value="expert-uuid",required = false) UUID expertUuid ,@RequestBody MessageDto meassage,@RequestParam(value = "email-context")
      EmailContext emailContext) {
     adminService.sendEmail(expertUuid,meassage,emailContext);
  }


}
