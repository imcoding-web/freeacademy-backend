package fr.imcoding.edu365.security;

import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.persistence.entities.Role;
import fr.imcoding.edu365.persistence.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private UUID uuid;

	private String username;

	@JsonIgnore
	private String userPassword;


	private AccountStatus accountStatus;

	private boolean isValidate;
	private boolean isElligibleToDoCourses;



	private Collection<? extends GrantedAuthority> authorities;
	@JsonIgnore
	private boolean isFirstConnecion;

	public static UserDetailsImpl buildForInformationGiver(InformationGiver user) {
		ArrayList<Role> userRoles = new ArrayList<>();
		userRoles.add(user.getUserRole());
		List<GrantedAuthority> authorities = userRoles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleCode().toString())).collect(Collectors.toList());
		return new UserDetailsImpl(user.getUuid(), user.getUserEmail(), user.getUserPassword(), user.getAccountStatus(),user.isValidate(),user.isElligibleToDoCourses(),
				authorities,user.isFirstConnexion());
	}
	public static UserDetailsImpl buildForUser(User user) {
		ArrayList<Role> userRoles = new ArrayList<>();
		userRoles.add(user.getUserRole());
		List<GrantedAuthority> authorities = userRoles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleCode().toString()))
				.collect(Collectors.toList());
		return  UserDetailsImpl.builder().uuid(user.getUuid()).userPassword(user.getUserPassword()).accountStatus(user.getAccountStatus())
				.authorities(authorities).isFirstConnecion(user.isFirstConnexion()).username(user.getUserLogin()).build();
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.userPassword;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
		//return this.accountStatus == AccountStatus.ACTIVE;
	}

}