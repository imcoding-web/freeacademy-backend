package fr.imcoding.edu365.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 08/01/2023
 */

@Entity
@Table(name = "edu365_prestation_meeting")
@Data
@NoArgsConstructor
public class PrestationMeeting extends BaseEntity {

  private String meetingJoinUrl;
  private Long meetingId;
  private String meetingCodeSecret;
  @OneToOne
  private Prestation prestation;



}
