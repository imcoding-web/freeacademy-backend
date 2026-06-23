package fr.imcoding.edu365.business.ext.sobflous.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/08/2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SobflousTokenRequest {
  private String ID_CLIENT_M;
  private String TRANSM_ID;
  private String AMOUNT;
  private String TOKEN;
  private Integer BONUS_VARIABLE;
  private String SHOP_ID;
}
