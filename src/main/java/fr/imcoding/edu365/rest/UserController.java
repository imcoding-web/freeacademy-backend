package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.client.dtos.response.StudentExsitVerificationResponse;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.InformationGiverService;
import fr.imcoding.edu365.business.services.InformationSeekerService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.client.dtos.request.ChangePasswordRequestDto;
import fr.imcoding.edu365.client.dtos.request.ForgetPasswordRequestDto;
import fr.imcoding.edu365.client.dtos.request.ResetPasswordRequestDto;
import fr.imcoding.edu365.client.dtos.request.UserRequest;
import fr.imcoding.edu365.client.dtos.response.TeacherExsitVerificationResponse;
import fr.imcoding.edu365.dtos.EmailVerifyDto;
import fr.imcoding.edu365.dtos.UserSkillsDto;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.User;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private  final InformationGiverService informationGiverService;
  private final InformationSeekerService informationSeekerService;
  @GetMapping()
  public List<User> getAllUsers() {
    return this.userService.getAllUsers();
  }

  @PostMapping("/add-new-user")
  public void saveOwner(@RequestBody UserRequest userRequest) {
    if (userRequest.getRole() == RoleCode.INFORMATION_GIVER) {
       this.informationGiverService.saveUser(userRequest);
    } else if (userRequest.getRole() == RoleCode.INFORMATION_SEEKER) {
       this.informationSeekerService.saveUser(userRequest);
    }
  }

  @PutMapping(value = "/activate")
  public User activateUserAccount(@RequestParam("code") String code) {
    return userService.activateUserAccount(code);
  }

  @PostMapping(value = "/verify")
  public Boolean checkUserEmail(@NotEmpty @Email @RequestBody EmailVerifyDto emailVerify) {
    return userService.checkUserEmail(emailVerify.getUserEmail());
  }
  
  @PostMapping(value = "/verify-teacher")
  public TeacherExsitVerificationResponse checkTeacherUserEmail(@NotEmpty @Email @RequestBody EmailVerifyDto emailVerify) {
    return userService.checkTeacherUserEmail(emailVerify.getUserEmail());
  }

  @GetMapping(value = "/verify-phone-number")
  public Boolean checkUserPhoneNumber(@NotEmpty @RequestParam("user_phone_number") String userPhoneNumber) {
    return userService.checkUserPhoneNumber(userPhoneNumber);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PostMapping("/update-skills")
  public User updateUserSkills(@RequestBody List<String> skills) {
    return this.userService.updateUserSkills(skills);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping(value = "/my-skills")
  public List<UserSkillsDto> getUserSkills() {
    return this.userService.getUserSkills();
  }

  @PostMapping(value = "/change-password")
  public void changeUserPassword(
      @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
    userService.changeUserPassword(changePasswordRequestDto);
  }

  @GetMapping(value = "/deactivate-account")
  public void deactivateUserAccount() {
    userService.deactivateUserAccount();
  }


  @PostMapping(value = "/forget-password", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void forgetPassword(
      @RequestBody ForgetPasswordRequestDto forgetPasswordRequestDto) {
    userService.sendForgetPasswordEmail(forgetPasswordRequestDto);
  }

  @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void resetPassword( @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
    userService.resetPassword(resetPasswordRequestDto);
  }

  @PostMapping(value = "/verify-student")
  public StudentExsitVerificationResponse checkStudentUserEmail(@NotEmpty @Email @RequestBody EmailVerifyDto emailVerify) {
    return userService.checkStudentUserEmail(emailVerify.getUserEmail());
  }
}
