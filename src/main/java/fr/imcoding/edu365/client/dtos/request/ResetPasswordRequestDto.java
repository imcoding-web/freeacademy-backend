package fr.imcoding.edu365.client.dtos.request;

import java.util.UUID;
import javax.validation.constraints.AssertTrue;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ResetPasswordRequestDto {


  private UUID requestUuid;


  private String password;

  private String confirmPassword;

  @AssertTrue()
  private boolean isValid() {
    return this.password.equals(this.confirmPassword);
  }
}
