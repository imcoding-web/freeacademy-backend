package fr.imcoding.edu365.client.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRequestDto {

  private String currentPassword;


  private String newPassword;


  private String confirmNewPassword;


}
