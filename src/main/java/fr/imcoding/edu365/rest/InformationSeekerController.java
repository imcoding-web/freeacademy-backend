package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.InformationSeekerService;
import fr.imcoding.edu365.client.dtos.response.UserResponse;
import fr.imcoding.edu365.dtos.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 23/07/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority({'INFORMATION_SEEKER'})")
public class InformationSeekerController {
  private final InformationSeekerService informationSeekerService;


  @GetMapping("/user-info")
  public UserResponse getUserInfo() {
    return this.informationSeekerService.getUserInfo();
  }
@PatchMapping("/update-user")
  public UserDetails patchUser(@RequestBody UserDetails userRequest) {
    return this.informationSeekerService.patchUser(userRequest);
  }



}
