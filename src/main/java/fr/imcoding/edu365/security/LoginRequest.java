package fr.imcoding.edu365.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class LoginRequest {

  private String email;

  @JsonAlias({"password", "userPassword"})
  private String userPassword;
}
