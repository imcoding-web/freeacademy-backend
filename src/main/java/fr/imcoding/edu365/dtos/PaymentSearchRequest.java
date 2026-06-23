package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.PaymentType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 08/12/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSearchRequest {
  private String startDate;
  private String endDate;
  private String paymentType;
  private String expertFirstName;
  private String expertLastName;




}
