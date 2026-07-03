package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.UserAvailabilityService;
import fr.imcoding.edu365.client.dtos.request.UserAvailabilityRequestDto;
import fr.imcoding.edu365.client.dtos.response.UserAvailabilityResponseDto;
import fr.imcoding.edu365.persistence.entities.UserAvailability;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 03/07/2022
 */

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
@CrossOrigin
public class UserAvailabilityController {

  private final UserAvailabilityService userAvailabilityService;
  @PostMapping()
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  public List<UserAvailability> createUserAvailability(
      @RequestBody List<UserAvailabilityRequestDto> userAvailabilityRequestDto) {
    return userAvailabilityService.createUserAvailability(userAvailabilityRequestDto);
  }

  @GetMapping()
  public List<UserAvailabilityResponseDto> getUserSpecialities() {
    return userAvailabilityService.getAllUserAvailabilities();
  }

  @PostMapping("/user-availability-by-day")
  public List<Integer> getUserAvailabilitiesByDay(@RequestBody UserAvailabilityRequestDto availabilityRequestDto) {
    return userAvailabilityService.getUserAvailabilityByDay(availabilityRequestDto);
  }

  @DeleteMapping(value = "/{userAvailabilityUuid}")
  public void deleteUserAvailability(@PathVariable UUID userAvailabilityUuid) {
    userAvailabilityService.deleteUserAvailability(userAvailabilityUuid);
  }
}
