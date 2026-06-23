package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.MediaContext;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 23/06/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDetails {
  private UUID mediaUuid;
  private String mediaUrl;
  private String mediaLabel;
  private String originalName;

}
