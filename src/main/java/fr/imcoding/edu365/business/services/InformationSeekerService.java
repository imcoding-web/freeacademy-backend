package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.AddressMapper;
import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.business.mappers.UserMapper;
import fr.imcoding.edu365.client.dtos.request.UserRequest;
import fr.imcoding.edu365.client.dtos.response.UserResponse;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.Role;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.InformationSeekerRepository;
import fr.imcoding.edu365.persistence.repositories.RoleRepository;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
      UUID uuid = userRepository.save(user).getUuid();
      userService.sendActivationEmail(uuid);
    }
    }
  public UserResponse getUserInfo() {
    User currentUser =  userService.getCurrentUser();
    return (currentUser != null ? userMapper.toUserResponse(currentUser) : null);
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
}
