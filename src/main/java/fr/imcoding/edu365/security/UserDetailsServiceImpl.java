package fr.imcoding.edu365.security;


import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.Role;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  @Transactional
  public UserDetails loadUserByUsername(String loginUserUSer) throws UsernameNotFoundException {
    User user = userRepository.findByUserEmail(loginUserUSer)
        .orElseThrow(
            () -> new UsernameNotFoundException("User Not Found with username: " + loginUserUSer));
    return user.getUserRole().getRoleCode().equals(RoleCode.INFORMATION_GIVER)?UserDetailsImpl.buildForInformationGiver((InformationGiver)user):UserDetailsImpl.buildForUser(user);
  }

  @Transactional
  public Role getRolesUser(String loginUserUSer) {
    User user = userRepository.findByUserEmail(loginUserUSer)
        .orElseThrow(
            () -> new UsernameNotFoundException("User Not Found with username: " + loginUserUSer));
    return user.getUserRole();
  }
}