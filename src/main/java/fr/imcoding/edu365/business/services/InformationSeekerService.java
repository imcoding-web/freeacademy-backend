package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.AddressMapper;
import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.business.mappers.SkillAreaSectionMapper;
import fr.imcoding.edu365.business.mappers.UserMapper;
import fr.imcoding.edu365.client.dtos.request.UserRequest;
import fr.imcoding.edu365.client.dtos.response.UserResponse;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.PackageSubscription;
import fr.imcoding.edu365.persistence.entities.Role;
import fr.imcoding.edu365.persistence.entities.SkillSubscription;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.InformationSeekerRepository;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import fr.imcoding.edu365.persistence.repositories.RoleRepository;
import fr.imcoding.edu365.persistence.repositories.SkillSubscriptionRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.imcoding.edu365.persistence.repositories.SubscribtionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class InformationSeekerService {

  private final InformationSeekerRepository userRepository;

  private final RoleRepository roleRepository;

  private final AddressMapper addressMapper;

  private final PasswordEncoder encoder;

  private final UserService userService;

  private final UserMapper userMapper;

  private final SkillAreaMapper skillAreaMapper;
  private final SkillAreaSectionMapper skillAreaSectionMapper;
  private final PackageSubscriptionRepository subscriptionRepository;
  private final SkillSubscriptionRepository skillSubscriptionRepository;

  public void saveUser(UserRequest userIn) {
    if(!userService.checkUserPhoneNumber(userIn.getUserPhoneNumber()) &&
        !userService.checkUserEmail(userIn.getUserEmail())) {
      InformationSeeker user = new InformationSeeker();
      user.setUserEmail(userIn.getUserEmail());
      user.setUserFirstName(userIn.getUserFirstName());
      user.setUserLastName(userIn.getUserLastName());
      user.setUserPassword(encoder.encode(userIn.getUserPassword()));
      user.setUserPhoneNumber(userIn.getUserPhoneNumber());

      Role lodgerRole = this.roleRepository.findByRoleCode(RoleCode.INFORMATION_SEEKER);
      user.setUserRole(lodgerRole);
      user.setUserInscriptionDate(new Date());
      user.setCurrentLevel(skillAreaMapper.toSkillArea(userIn.getSkillLevel()));
      user.setCurrentLevelSection(skillAreaSectionMapper.toSkillAreaSection(userIn.getSkillLevelSection()));
      UUID uuid = userRepository.save(user).getUuid();
      userService.sendActivationEmail(uuid);
    }
    }
  @Transactional
  public UserResponse getUserInfo() {
    User currentUser =  userService.getCurrentUser();
    InformationSeeker student = (InformationSeeker) currentUser;
    PackageSubscription packageSubscription = findByByStudentAndSubscriptionStatus(student, PackageStatus.ACTIVE);
    if(packageSubscription == null) {
      List<SkillSubscription> skillSubscriptions = skillSubscriptionRepository.findByStudentAndSubscriptionStatus(student, PackageStatus.ACTIVE);
      return (currentUser != null ? userMapper.toUserResponse(currentUser, skillSubscriptions) : null);
    }
    return (currentUser != null ? userMapper.toUserResponse(currentUser, packageSubscription) : null);
  }
  public UserDetails patchUser(UserDetails userRequest) {
    InformationSeeker user = (InformationSeeker) userService.getCurrentUser();
    user.setUserFirstName(userRequest.getUserFirstName());
    user.setUserLastName(userRequest.getUserLastName());
    user.setUserEmail(userRequest.getUserEmail());
    user.setUserAddress(
        userRequest.getUserAddress() != null ? addressMapper.toAddress(userRequest.getUserAddress())
            : null);
    userRepository.save(user);
    return userMapper.toUserDetailsResponse(user);
  }

  public boolean checkPack() {
      InformationSeeker student = (InformationSeeker) userService.getCurrentUser();
      boolean isHasAPack =  subscriptionRepository.existsByStudentAndSubscriptionStatus(student,
              PackageStatus.ACTIVE);
      if (!isHasAPack) { // temporairement: après on doit pas passser l'etudiant s'il a pas un pack dans ce matiére
        return !skillSubscriptionRepository.findByStudentAndSubscriptionStatus(student,
            PackageStatus.ACTIVE).isEmpty();
      }
      return isHasAPack;
    }
    public PackageSubscription findByByStudentAndSubscriptionStatus(InformationSeeker student, PackageStatus subscriptionStatus) {
      return subscriptionRepository.findByStudentAndSubscriptionStatus(student, subscriptionStatus);
    }

}
