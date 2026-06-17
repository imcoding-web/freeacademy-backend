package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.VideoService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 01/07/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

  private final VideoService videoService;

  @GetMapping("/stream/{mediaUuid}")
  @CrossOrigin
  public ResponseEntity<byte[]> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
      @PathVariable("mediaUuid") UUID mediaUuid) throws Exception {
    return videoService.prepareContent(mediaUuid, httpRangeList);
  }

}
