package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.business.mappers.SkillMapper;
import fr.imcoding.edu365.business.mappers.UserMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.request.ChangePasswordRequestDto;
import fr.imcoding.edu365.client.dtos.request.ForgetPasswordRequestDto;
import fr.imcoding.edu365.client.dtos.request.ResetPasswordRequestDto;
import fr.imcoding.edu365.client.dtos.response.StudentExsitVerificationResponse;
import fr.imcoding.edu365.client.dtos.response.TeacherExsitVerificationResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.dtos.UserSkillsDto;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.exceptions.NotAllowedOperationException;
import fr.imcoding.edu365.persistence.entities.*;
import fr.imcoding.edu365.persistence.repositories.InformationGiverRepository;
import fr.imcoding.edu365.persistence.repositories.InformationSeekerRepository;
import fr.imcoding.edu365.persistence.repositories.UserRepository;
import fr.imcoding.edu365.utils.Constants;
import fr.imcoding.edu365.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.imcoding.edu365.utils.Constants.CODE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  
  private final InformationGiverRepository informationGiverRepository;
  private final InformationSeekerRepository informationSeekerRepository;


  private final UserMapper userMapper;

  private final EmailService emailService;

  private final AccountActivationService accountActivationService;
  private final SkillService skillService;

  private final SkillMapper skillMapper;
  private final SkillAreaMapper skillAreaMapper;


  private final PasswordEncoder encoder;

  private final ResetPasswordService resetPasswordService;




  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }

  public User getUserByUUID(UUID userUuid) {
    return this.userRepository.findByUuid(userUuid).orElse(null);
  }

  public User saveOrUpdateUser(User user) {
    return this.userRepository.save(user);
  }

  public void deleteUser(UUID userUuid) {
    User user = getUserByUUID(userUuid);
    this.userRepository.deleteById(user.getId());
  }
  
  public UserDetails getUserInfo() {
    User user = getCurrentUser();
    return user != null ? userMapper.toUserDetailsResponse(getCurrentUser()) : null;
  }

  public User getCurrentUser() {
    UUID currentuserId = SecurityUtil.getCurrentUserUuid();
    User user = getUserByUUID(currentuserId);
    return user;
  }
public List<User> getUserDetailsyRole(RoleCode roleCode){
  return userRepository.findByUserRoleRoleCode(roleCode);

}
 /* public ExpertDetails patchUser(ExpertDetails userRequest) {
    User user =(InformationGiver) getCurrentUser();
    user.setUserFirstName(userRequest.getUserFirstName());
    user.setUserLastName(userRequest.getUserLastName());
    user.setUserEmail(userRequest.getUserEmail());
    user.setUserAddress(
        userRequest.getUserAddress() != null ? addressMapper.toAddress(userRequest.getUserAddress())
            : null);
    ((InformationGiver) user).setIdentityType(userRequest.getUserIdentityType());
    ((InformationGiver) user).setIdentityNumber(userRequest.getUserIdentityNumber());


    return userRepository.save(user);
  }*/


  public User activateUserAccount(String activationCode) {
    log.info("Activate User Account with UUID: {}", activationCode);
    AccountActivation accountActivation = accountActivationService
        .getByAccountActivationCode(activationCode);
    User user = accountActivation.getUser();
    if (!user.isUserIsActif()) {
      user.setAccountStatus(AccountStatus.ACTIVE);;

      userRepository.saveAndFlush(user);
      accountActivationService.removeAccountActication(accountActivation);
    }

    return user;
  }

  public void sendActivationEmail(UUID userUuid) {
    User user = getUserByUUID(userUuid);
    String activationCode = accountActivationService.saveAccountActivation(user);
    Map<String, Object> userMaps = new HashMap<>();
    userMaps.put(CODE, activationCode);
    EmailDto userEmailDto = new EmailDto( Constants.MAIL_SUBJECT_USER_WELCOME,
        user.getUserRole().getRoleCode()==RoleCode.INFORMATION_GIVER ? "welcome-expert.html" : "welcome-user.html" , userMaps, new HashMap<>(),EmailContext.WELCOME_USER);
    emailService.sendMail(userEmailDto, Stream.of(user.getUserEmail()).collect(Collectors.toList()));
  }

  public Boolean checkUserEmail(String userEmail) {
    log.info("Check existence of User Email: {}", userEmail);
    return userRepository.findByUserEmail(userEmail).isPresent();
  }

  public Boolean checkUserPhoneNumber(String PhoneNumber) {
    log.info("Check existence of User Phone Number: {}", PhoneNumber);
    return userRepository.findByUserPhoneNumber(PhoneNumber).isPresent();
  }
  public User getByUserEmail(String userEmail) {
    log.info("Check existence of User Email: {}", userEmail);
    return userRepository.findByUserEmail(userEmail).orElse(null);
  }

  public User updateUserSkills(List<String> skillsCode) {
    InformationGiver currentUser = (InformationGiver) getCurrentUser();
    currentUser.getSkills().removeAll(currentUser.getSkills());

    skillsCode.stream().forEach(skillCode -> {
      Skill skill = skillService.getSkillsBySkillCode(skillCode);
      currentUser.getSkills().add(skill);
    });
    return userRepository.save(currentUser);
  }

  public List<UserSkillsDto> getUserSkills() {
    InformationGiver currentUser = (InformationGiver) getCurrentUser();
    Map<SkillArea, List<Skill>> skillsBySkillArea = currentUser.getSkills().stream()
        .collect(Collectors.groupingBy(Skill::getSkillArea));
    return skillsBySkillArea.entrySet().stream()
        .map(e ->
        { return new UserSkillsDto(e.getKey().getSkillAreaCode(),e.getKey().getSkillAreaLabel(),
            e.getValue().stream().map(s -> (skillMapper.toSkillDto(s))).collect(Collectors.toList()));

        }).collect(Collectors.toList());

  }

  public List<UserSkillsDto> getSkillsUserUuid(UUID userUuid) {
    InformationGiver currentUser = (InformationGiver) getUserByUUID(userUuid);
    Map<SkillArea, List<Skill>> skillsBySkillArea = currentUser.getSkills().stream()
        .collect(Collectors.groupingBy(Skill::getSkillArea));
    return skillsBySkillArea.entrySet().stream()
        .map(e ->
        { return new UserSkillsDto(e.getKey().getSkillAreaCode(),e.getKey().getSkillAreaLabel(),
            e.getValue().stream().map(s -> (skillMapper.toSkillDto(s))).collect(Collectors.toList()));

        }).collect(Collectors.toList());

  }



  @Transactional
  public void changeUserPassword(ChangePasswordRequestDto changePasswordRequestDto) {
    User currentUser = getCurrentUser();
    if (!encoder.matches(
        changePasswordRequestDto.getCurrentPassword(), currentUser.getUserPassword())) {

      throw new NotAllowedOperationException("Current password is not correct");
    }

    currentUser.setUserPassword(encoder.encode(changePasswordRequestDto.getNewPassword()));
    userRepository.saveAndFlush(currentUser);
  }

  @Transactional
  public void deactivateUserAccount() {
    User currentUser = getCurrentUser();
    log.info(
        "Deactivate User Account with UUID: {}", currentUser.getUuid());
    currentUser.setAccountStatus(AccountStatus.BLOCKED);
    currentUser.setLastDeactivationDate(new Date());
    userRepository.saveAndFlush(currentUser);
  }


  @Transactional
  public void sendForgetPasswordEmail(ForgetPasswordRequestDto dto) {
    log.info("Send ForgetPassword request to: {}", dto.getUserEmail());
    User user = userRepository.findByUserEmail(dto.getUserEmail()).orElse(null);
    if(user!=null) {
      ResetPassword resetPassword = resetPasswordService.createResetPassword(user);

      Map<String, Object> maps = new HashMap<>();
      maps.put("requestUuid", resetPassword.getResetPasswordUuid().toString());

      EmailDto emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_USER_RESET, "reset-password.html", maps,
              new HashMap<>(),EmailContext.RESET_PWD);

      emailService.sendMail(emailDto, Stream.of(dto.getUserEmail()).collect(Collectors.toList()));
    }
  }

  @Transactional
  public void resetPassword(ResetPasswordRequestDto dto) {
    ResetPassword resetPassword = resetPasswordService.getByResetPasswordUuid(dto.getRequestUuid());
    User user = resetPassword.getResetPasswordUser();
    user.setUserPassword(encoder.encode(dto.getPassword()));
    userRepository.saveAndFlush(user);
    resetPasswordService.removeResetPassword(resetPassword);
  }

  public void updateUserFirstCnx() {
    User user=getCurrentUser();
   if(user.isFirstConnexion()) {
     user.setFirstConnexion(false);
     userRepository.saveAndFlush(user);
   }
  }

	public TeacherExsitVerificationResponse checkTeacherUserEmail(String userEmail) {
		InformationGiver teacher = informationGiverRepository.findByUserEmail(userEmail).orElse(null);
		if (teacher == null)
			return TeacherExsitVerificationResponse.builder().exist(false).build();
		return new TeacherExsitVerificationResponse(true, teacher.getUserFirstName(), teacher.getUserLastName(),
				teacher.getUserFirstName() + " " + teacher.getUserLastName());
	}



  public StudentExsitVerificationResponse checkStudentUserEmail(String userEmail) {
    InformationSeeker student = informationSeekerRepository.findByUserEmail(userEmail).orElse(null);
    if (student == null)
      return StudentExsitVerificationResponse.builder().exist(false).build();
    return new StudentExsitVerificationResponse(true, student.getUserFirstName(), student.getUserLastName(),
        student.getUserFirstName() + " " + student.getUserLastName(),student.getCurrentLevel()!=null?skillAreaMapper.toSkillAreaDto(student.getCurrentLevel()):null,student.getUuid());
  }
}
