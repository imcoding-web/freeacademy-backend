package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.UserAvailabilityMapper;
import fr.imcoding.edu365.client.dtos.request.UserAvailabilityRequestDto;
import fr.imcoding.edu365.client.dtos.response.UserAvailabilityResponseDto;
import fr.imcoding.edu365.enumeration.DaysOfWeek;
import fr.imcoding.edu365.exceptions.NotFoundException;
import fr.imcoding.edu365.exceptions.UserForbiddenException;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserAvailability;
import fr.imcoding.edu365.persistence.repositories.UserAvailabilityRepository;
import fr.imcoding.edu365.utils.DatesUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 03/07/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAvailabilityService {
  private final UserAvailabilityRepository userAvailabilityRepository;
  private final UserService userService;
  private final UserAvailabilityMapper userAvailabilityMapper;

  @Transactional
  public List<UserAvailability> createUserAvailability(
      List<UserAvailabilityRequestDto> availabilityDto) {
    InformationGiver user = (InformationGiver) userService.getCurrentUser();

    log.info(
        "Create new Availability for Connected User with UUID: {}",
        user.getUuid());
    List<UserAvailability> response = new ArrayList<>();
    List<UUID> existingAvailability = getAllUserAvailabilities().stream().map(UserAvailabilityResponseDto::getUserAvailabilityUuid).collect(Collectors.toList());
    List<UserAvailability> listToRepersist =new ArrayList<>();

    availabilityDto.stream().forEach(expertAvailability->{

      List<UserAvailability> availabilities =
          userAvailabilityRepository.findSimilarUserAvailability(
              user,
              DaysOfWeek.valueOf(expertAvailability.getUserAvailabilityDay()),
              expertAvailability.getUserAvailabilityStartDate(),
              expertAvailability.getUserAvailabilityEndDate());
      listToRepersist.addAll(
          availabilities.stream()
              .filter(e ->
                existingAvailability.contains(e.getUuid()))
              .collect(Collectors.toList()));
      UserAvailability availability = availabilities.isEmpty() ? null : availabilities.get(0);
      if (availability == null) {
        availability = new UserAvailability();
        availability.setUserAvailabilityTitle(expertAvailability.getUserAvailabilityTitle());
        availability.setUserAvailabilityStartDate(
            expertAvailability.getUserAvailabilityStartDate());
        availability.setUserAvailabilityEndDate(
            expertAvailability.getUserAvailabilityEndDate());
        availability.setUserAvailabilityDay(DaysOfWeek.valueOf(expertAvailability.getUserAvailabilityDay()));
        availability.setUserAvailabilityExpert(user);
      } else {
        availability.setUserAvailabilityStartDate(
            DatesUtils.min(
                expertAvailability.getUserAvailabilityStartDate(),
                expertAvailability.getUserAvailabilityStartDate()));

        availability.setUserAvailabilityEndDate(
            DatesUtils.max(
                expertAvailability.getUserAvailabilityEndDate(),
                expertAvailability.getUserAvailabilityEndDate()));
      }

      userAvailabilityRepository.saveAndFlush(availability);
      response.add(availability);

    });

    existingAvailability.stream()
        .filter(f -> !listToRepersist.stream()
            .anyMatch(s -> f.equals(s.getUuid())
                ))
        .collect(Collectors.toList()).forEach(item->{userAvailabilityRepository.deleteByUuid(item);});;
    return response;
  }


  @Transactional
  public List<UserAvailabilityResponseDto> getAllUserAvailabilities() {
    InformationGiver user = (InformationGiver) userService.getCurrentUser();

    log.info("Get All User Availability with User UUID: {}", user.getUuid());
    return userAvailabilityRepository
        .findAllByUserAvailabilityExpertUuid(user.getUuid())
        .stream()
        .map(
            availability -> {
              return userAvailabilityMapper.toUserAvailabilityDto(availability);

            })
        .collect(Collectors.toList());
  }


  public UserAvailability getByAvailabilityUuid(UUID availabilityUuid) {
    log.info("Get Availability with ID: {}", availabilityUuid);
    return userAvailabilityRepository
        .findByUuid(availabilityUuid);

  }
  @Transactional
  public void deleteUserAvailability(UUID userAvailabilityUuid) {
    log.info("Delete Availability with ID: {}", userAvailabilityUuid);
    UserAvailability userAvailability = getByAvailabilityUuid(userAvailabilityUuid);
    if (!userAvailability
        .getUserAvailabilityExpert()
        .getUuid()
        .equals(userService.getCurrentUser().getUuid())) {
      throw new UserForbiddenException(
          "Forbidden: authenticated user does not have permission to delete this availability");
    }

    userAvailabilityRepository.delete(userAvailability);
  }


  public List<Integer> getUserAvailabilityByDay(
      UserAvailabilityRequestDto availabilityDto) {
      InformationGiver expert =(InformationGiver)  userService.getUserByUUID(availabilityDto.getExpertUuid());

    log.info(
        "get Availability by day for User with UUID: {}",
        expert.getUuid());
    List<Integer> hours=new ArrayList<>();
    List<UserAvailability> availabilities =
        userAvailabilityRepository.findSimilarUserAvailabilityByDay(
            expert,
            DaysOfWeek.valueOf(availabilityDto.getUserAvailabilityDay()));

            availabilities   .stream()
                .map(
                    availability -> {
                      hours.addAll(getAllHours(availability.getUserAvailabilityStartDate(),availability.getUserAvailabilityEndDate(),availabilityDto.getVideoconferenceDuration()));
                      return hours;

                    })
                .collect(Collectors.toList());
            return hours;

  }

  public  List<Integer> getAllHours(Date startDate, Date endDate,int duration) {
    List<Integer> hours=new ArrayList<>();
    if(DatesUtils.getHourDurationsBetween(startDate, endDate)>=duration){
      for(int i=startDate.getHours();i<endDate.getHours();i++){
        if(i+duration <= endDate.getHours()) {
          hours.add(i);
        }
      }
    }

    return hours;
  }



  @Transactional
  public List<UserAvailabilityResponseDto> getUserAvailabilitiesByUserUuid(UUID uuid) {
    InformationGiver user = (InformationGiver) userService.getUserByUUID(uuid);

    log.info("Get All User Availability with User UUID: {}", user.getUuid());
    return userAvailabilityRepository
        .findAllByUserAvailabilityExpertUuid(user.getUuid())
        .stream()
        .map(
            availability -> {
              return userAvailabilityMapper.toUserAvailabilityDto(availability);

            })
        .collect(Collectors.toList());
  }

  }
