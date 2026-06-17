package fr.imcoding.edu365.business.services.zoomMeeting;

import fr.imcoding.edu365.business.ext.zoom.response.ZoomMeetingsDTO;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fr.imcoding.edu365.business.ext.zoom.response.ZoomAuthTokenDTO;
import fr.imcoding.edu365.business.ext.zoom.response.ZoomMeetingObjectDTO;
import fr.imcoding.edu365.business.ext.zoom.response.ZoomMeetingSettingsDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rokaya
 * @Date 29/12/2022
 */
@Service
@Slf4j
public class ZoomMeetingService {

  @Value("${edu365.zoom.meeting.token}")
  private String token;
  
  @Value("${edu365.zoom.meeting.accountId}")
  private String accountId;
  
  @Value("${edu365.zoom.meeting.clientId}")
  private String clientId;
  
  @Value("${edu365.zoom.meeting.clientSecret}")
  private String clientSecret;


  @Value("${edu365.zoom.meeting.api.base.url}")
  private String requestZoomMeetingUrl;
  
  @Value("${edu365.zoom.s2sToken.api.base.url}")
  private String requestZoomTokenUrl;


  public ZoomMeetingObjectDTO createMeeting(Integer meetingDuration,String topic) {
    log.debug("Request to create a Zoom meeting");
    if(meetingDuration == null) meetingDuration = 2;
   ZoomMeetingObjectDTO zoomMeetingObjectDTO=new ZoomMeetingObjectDTO();
    ZoomMeetingSettingsDTO settingsDTO = new ZoomMeetingSettingsDTO();
    settingsDTO.setJoin_before_host(true);
    settingsDTO.setParticipant_video(true);
    settingsDTO.setHost_video(false);
    settingsDTO.setAuto_recording("cloud");
    settingsDTO.setMute_upon_entry(true);
    settingsDTO.setWatermark(true);
    settingsDTO.setRegistration_type(0);
    zoomMeetingObjectDTO.setTopic(topic);
    //zoomMeetingObjectDTO.setDuration(DatesUtils.convertHourToMinutes(meetingDuration));
    zoomMeetingObjectDTO.setSettings(settingsDTO);

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    ZoomAuthTokenDTO tokenDto = getZoomAuthToken();
    headers.add("Authorization", "Bearer "+tokenDto.getAccessToken());
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ZoomMeetingObjectDTO> httpEntity = new HttpEntity<ZoomMeetingObjectDTO>(
        zoomMeetingObjectDTO, headers);
    ResponseEntity<ZoomMeetingObjectDTO> zEntity = restTemplate.exchange(requestZoomMeetingUrl, HttpMethod.POST, httpEntity, ZoomMeetingObjectDTO.class);
    System.out.println("response::"+zEntity.getBody());

    if (zEntity.getStatusCodeValue() == 201) {
      log.debug("Zooom meeeting response {}", zEntity);
      return zEntity.getBody();
    } else {
      log.debug("Error while creating zoom meeting {}", zEntity.getStatusCode());
    }
    System.out.println("response::"+zoomMeetingObjectDTO);

    return zoomMeetingObjectDTO;
  }

  public List<ZoomMeetingObjectDTO> getLiveMeetings() {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    ZoomAuthTokenDTO tokenDto = getZoomAuthToken();
    headers.add("Authorization", "Bearer "+tokenDto.getAccessToken());
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ZoomMeetingObjectDTO> httpEntity = new HttpEntity<ZoomMeetingObjectDTO>(
        headers);
    try {
      ResponseEntity<ZoomMeetingsDTO> response = restTemplate.exchange("https://api.zoom.us/v2/users/me/meetings?type=live", HttpMethod.GET, httpEntity, ZoomMeetingsDTO.class);
      return response.getBody().getMeetings();
    } catch (Exception e) {
      System.out.println(e);
    }

  return new ArrayList<>();

  }
  
  private ZoomAuthTokenDTO getZoomAuthToken() {
	  RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = createBasicAuthHeaders(clientId, clientSecret);
	    String s2sZoomUrl = this.requestZoomTokenUrl + "?grant_type=account_credentials&account_id=" + this.accountId;
	    HttpEntity httpEntity = new HttpEntity<>(
	    		null, headers);
	    ResponseEntity<ZoomAuthTokenDTO> response = restTemplate.exchange(s2sZoomUrl, HttpMethod.POST, httpEntity, ZoomAuthTokenDTO.class);
	  return response.getBody();
  }
  
	private HttpHeaders createBasicAuthHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}

}
