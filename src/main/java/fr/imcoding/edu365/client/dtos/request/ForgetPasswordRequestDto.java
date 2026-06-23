package fr.imcoding.edu365.client.dtos.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgetPasswordRequestDto {
  private String userEmail;
}
