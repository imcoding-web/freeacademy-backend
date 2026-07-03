package fr.imcoding.edu365.security;

import lombok.Data;

@Data
public class LoginRequest {

  private String email;

  private String userPassword;
}
