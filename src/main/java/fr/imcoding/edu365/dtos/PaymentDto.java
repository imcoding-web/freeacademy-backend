package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Offer;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 05/10/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
  private PaymentType paymentType;
  private UUID offerUuid;
  private MultipartFile paymentMedia;

}
