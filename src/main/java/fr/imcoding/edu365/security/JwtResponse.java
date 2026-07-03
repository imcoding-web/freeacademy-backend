package fr.imcoding.edu365.security;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.imcoding.edu365.enumeration.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {

  private String token;

  private UUID uuid;
  private String refreshToken;
  private String username;
  private List<String> roles;
  private AccountStatus accountStatus;
  private boolean isFirstConnexion;
  private boolean isValidate;
  private boolean isElligibleToDoCourses;


}
