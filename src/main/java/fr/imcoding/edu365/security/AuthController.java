package fr.imcoding.edu365.security;

import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.User;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.RefreshTokenService;
import fr.imcoding.edu365.client.dtos.response.RefreshJwResponse;
import fr.imcoding.edu365.enumeration.AccountStatus;
import lombok.RequiredArgsConstructor;


@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:2400"}, allowedHeaders = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


  private final AuthenticationManager authenticationManager;

  private final RefreshTokenService refreshTokenService;

  private final JwtUtils jwtUtils;

	private final UserService userService;


	@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:2400"}, allowedHeaders = "*", allowCredentials = "true")
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		User user=userService.getByUserEmail(loginRequest.getEmail());
		if(user!=null && !user.getAccountStatus().equals(AccountStatus.ACTIVE)){
	userService.sendActivationEmail(user.getUuid());

}
    Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getUserPassword()));
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//if (userDetails.getAccountStatus().equals(AccountStatus.ACTIVE)) {
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
			.collect(Collectors.toList());
		if (!userDetails.isElligibleToDoCourses() && userDetails.getAuthorities().iterator().next().toString().equalsIgnoreCase(RoleCode.INFORMATION_GIVER.toString()) ) {
			return ResponseEntity.ok().body(JwtResponse.builder()
					.username(loginRequest.getEmail()).isElligibleToDoCourses(userDetails.isElligibleToDoCourses()).isValidate(userDetails.isValidate()).accountStatus(userDetails.getAccountStatus()).roles(roles).uuid(userDetails.getUuid()).build());

		} else {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtUtils.generateJwtToken(authentication);
			String refreshToken = jwtUtils
					.generateRefreshToken(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
      userService.updateUserFirstCnx();
			return ResponseEntity.ok(new JwtResponse(token, userDetails.getUuid(), refreshToken,
					userDetails.getUsername(), roles, AccountStatus.ACTIVE,userDetails.isFirstConnecion(),userDetails.isValidate(),userDetails.isElligibleToDoCourses()));
		}
	}


  @PostMapping("/refresh-token")

  public ResponseEntity<RefreshJwResponse> refreshToken(
      @RequestHeader(value = "Refresh-token") String refreshToken) {
    RefreshJwResponse newToken = refreshTokenService.refreshUserToken(refreshToken);
    return ResponseEntity.ok(newToken);
  }
}



