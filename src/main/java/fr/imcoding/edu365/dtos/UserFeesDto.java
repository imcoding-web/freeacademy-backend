package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFeesDto {
  private PaymentType paymentType;
  private FeesType feesType;
  private MultipartFile paymentMedia;
  private String promotionCode;
}
