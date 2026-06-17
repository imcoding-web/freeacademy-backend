package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.client.dtos.response.UserAvailabilityResponseDto;
import fr.imcoding.edu365.persistence.entities.UserAvailability;
import fr.imcoding.edu365.utils.DatesUtils;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 03/07/2022
 */
@Service
public class UserAvailabilityMapper {
  public UserAvailabilityResponseDto toUserAvailabilityDto(UserAvailability availability) {
    return new UserAvailabilityResponseDto(availability.getUuid(), availability.getUserAvailabilityTitle()
        , DatesUtils.convertDateToLocalDate(availability.getUserAvailabilityStartDate()),DatesUtils.convertDateToLocalDate(availability.getUserAvailabilityEndDate()),availability.getUserAvailabilityDay());
  }
}
