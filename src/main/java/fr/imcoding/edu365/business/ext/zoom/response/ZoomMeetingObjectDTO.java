package fr.imcoding.edu365.business.ext.zoom.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 29/12/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ZoomMeetingObjectDTO {
  private Long id;

  private String uuid;

  private String assistant_id;

  private String host_email;

  private String registration_url;

  private String topic;

  private Integer type;

  private String start_time;

  private Integer duration;

  private String schedule_for;

  private String timezone;

  private String created_at;

  private String password;

  private String agenda;

  private String start_url;

  private String join_url;

  private String h323_password;

  private Integer pmi;
  private ZoomMeetingSettingsDTO settings;



}
