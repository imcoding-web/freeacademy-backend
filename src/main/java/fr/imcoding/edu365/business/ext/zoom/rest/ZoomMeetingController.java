package fr.imcoding.edu365.business.ext.zoom.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.ext.zoom.response.ZoomMeetingObjectDTO;
import fr.imcoding.edu365.business.services.zoomMeeting.ZoomMeetingService;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 29/12/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/zoom-meeting")
@RequiredArgsConstructor
public class ZoomMeetingController {
  private final ZoomMeetingService zoomMeetingService;

  /*@PostMapping
  public ZoomMeetingObjectDTO createMeeting(@RequestBody ZoomMeetingObjectDTO zoomMeetingObjectDTO) {
    return zoomMeetingService.createMeeting(zoomMeetingObjectDTO);

  }
*/
  @GetMapping
  public ZoomMeetingObjectDTO createMeeting() {
    
	   ZoomMeetingObjectDTO result = zoomMeetingService.createMeeting(10,"test");
	   return result;

  }

  }
