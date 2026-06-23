package fr.imcoding.edu365.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityDto {
  private UUID specialityUuid;
  private String specialityLabel;
  private String specialityCode;

}
