package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.enumeration.EmailContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 18/11/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotSentEmailResponse {
  private String email;
  private EmailContext context;
  private int attemptsNumber;
  private String subject;
  private String templateName;
  private String extraInformation;

}
