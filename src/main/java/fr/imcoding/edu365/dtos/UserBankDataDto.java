package fr.imcoding.edu365.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 27/12/2022
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserBankDataDto {
  private UUID expertUuid;
  private double ammount;

}
