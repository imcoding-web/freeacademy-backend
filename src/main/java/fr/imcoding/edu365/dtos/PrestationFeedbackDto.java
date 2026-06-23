package fr.imcoding.edu365.dtos;

import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/01/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestationFeedbackDto {
private int rate;
private String message;
}
