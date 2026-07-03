package fr.imcoding.edu365.business.services;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import fr.imcoding.edu365.business.services.files.FilesStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.mappers.AnnouncementMapper;
import fr.imcoding.edu365.business.mappers.MediaDatailsMapper;
import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.business.mappers.SkillMapper;
import fr.imcoding.edu365.business.mappers.UserMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.business.services.files.DBFileStorageService;
import fr.imcoding.edu365.client.dtos.request.AnnouncementRequest;
import fr.imcoding.edu365.client.dtos.response.AnnouncementFiltredResponseDto;
import fr.imcoding.edu365.client.dtos.response.AnnouncementResponse;
import fr.imcoding.edu365.client.dtos.response.MyAnnouncementResponse;
import fr.imcoding.edu365.dtos.AnnouncementDto;
import fr.imcoding.edu365.dtos.AnnouncementSearchCriteria;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.dtos.SimilarAnnouncementSearchCriteria;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.exceptions.BadRequestException;
import fr.imcoding.edu365.exceptions.UserForbiddenException;
import fr.imcoding.edu365.persistence.entities.Announcement;
import fr.imcoding.edu365.persistence.entities.InformationSeekerPackage;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.AnnouncementRepository;
import fr.imcoding.edu365.persistence.repositories.InformationGiverRepository;
import fr.imcoding.edu365.persistence.specifications.AnnouncementSpecifications;
import fr.imcoding.edu365.utils.AnnouncementUtils;
import fr.imcoding.edu365.utils.Constants;
import fr.imcoding.edu365.utils.DatesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementService {

  private final AnnouncementRepository announcementRepository;

  private final AnnouncementMapper announcementMapper;

  private final SkillAreaMapper skillAreaMapper;


  private final SkillMapper skillMapper;

  private final UserService userService;
  private final MediaDatailsMapper mediaDatailsMapper;
  private final FilesStorageService dBFileStorageService;
  private final MediaService mediaService;
  private final UserMapper userMapper;
  private final EmailService emailService;
  private final InformationGiverRepository informationGiverRepository;
  
  private final InformationSeekerPackageService informationSeekerPackageService;

  public List<Announcement> getAllAnnouncements() {

    return this.announcementRepository.findAll();
  }

  public Announcement getAnnouncementByUUID(UUID announcementUUID) {
    return this.announcementRepository.findByUuid(announcementUUID);
  }

  public AnnouncementDto getAnnouncementDetailsByUUID(UUID announcementUUID) {
    Announcement announcement = getAnnouncementByUUID(announcementUUID);
    return announcementMapper.toAnnouncementDto(announcement);
  }

  public Announcement save(Announcement announcement) {
    return this.announcementRepository.saveAndFlush(announcement);
  }

  public List<MyAnnouncementResponse> getMyAnnoucements() {
    User user = userService.getCurrentUser();

    return this.announcementRepository.findByAnnouncementPublisherUuid(user.getUuid()).stream().sorted(Comparator
        .comparing(Announcement::getCreatedAt).reversed())
        .map(announcement -> announcementMapper.toMyAnnouncementResponse(announcement))
        .collect(Collectors.toList());

  }

  @Transactional
  public Announcement saveAnnouncement(AnnouncementRequest announcementIn) {
    User user = userService.getCurrentUser();
    if (user != null) {
      Announcement announcement = new Announcement();
      announcement.setAnnouncementPublisher(user);
      announcement.setAnnouncementDescription(announcementIn.getAnnouncementDescription());
      announcement
          .setAnnouncementEndAvailableDate(announcementIn.getAnnouncementEndAvailableDate());
      announcement.setAnnouncementStatus(AnnouncementStatus.PENDING_VALIDATION);
      announcement.setAnnouncementTitle(announcementIn.getAnnouncementTitle());
      announcement.setAnnouncementType(announcementIn.getAnnouncementType());
      announcement
          .setAnnouncementSkillArea(skillAreaMapper.toSkillArea(announcementIn.getSkillArea()));
      announcement.setInterventionType(announcementIn.getInterventionType());
      announcement
          .setSkill(skillMapper.toSkill(announcementIn.getSkill()));
      announcement.setHoursNumber(announcementIn.getHoursNumber());
      announcement.setHomeService(announcementIn.isHomeService());
      announcement.setHomeServiceDetails(announcementIn.getHomeServiceDetails());
      announcement.setEstimatedPriceToPay(AnnouncementUtils.getEstimatedPriceToPay(announcement.getAnnouncementType(), announcement.getAnnouncementSkillArea()));
      announcement.setEstimatedHourNumber(AnnouncementUtils.getEstimatedHours(announcement.getAnnouncementType()));
      //test if the published has an active study package
      InformationSeekerPackage lastUserPackage = informationSeekerPackageService.findLastStudyPackageForAInformationGiver(announcement.getAnnouncementPublisher().getUuid());
      if(lastUserPackage != null && lastUserPackage.isActive() && lastUserPackage.isValid(announcement.getAnnouncementSkillArea())) {
      	announcement.setInStudyPackage(true);
      }
      announcement=announcementRepository.save(announcement);
      //commenter car on va envoyer la notif une fois l'annonce est validé
      /*List<String> destinations=informationGiverService.getExpertBySkillCode(announcementIn.getSkill().getSkillCode()).stream().map(InformationGiver::getUserEmail).collect(Collectors.toList());
      if(!destinations.isEmpty()) {
      sendNotificationEmail(announcement.getUuid(),destinations);
      }*/

      return announcement;
      //


      /*announcementMapper
          .toMyAnnouncementResponse(this.announcementRepository.save(announcement),new ArrayList<>());*/
    }
    return null;
  }

  public void deleteAnnouncement(Long id) {
    this.announcementRepository.deleteById(id);
  }

  public List<AnnouncementResponse> findLastAnnouncements() {
    return announcementRepository.findTop12ByOrderByCreatedAtDesc().stream()
        .map(announcement -> announcementMapper.toAnnouncementResponse(announcement))
        .collect(Collectors.toList());
  }

  public Announcement updateAnnouncement(AnnouncementDto announcementRequest) {
    List<Media> mediaList = checkAnouncementMedia(announcementRequest.getAnnouncementUuid(),
        announcementRequest.getMedias());
    Announcement announcementToUpdate = announcementMapper.toAnnouncement(announcementRequest);
    if (!announcementToUpdate.getAnnouncementPublisher().getUuid()
        .equals(userService.getCurrentUser().getUuid())) {
      throw new UserForbiddenException("Cannot update project for other user");
    }
    if (!mediaList.isEmpty()) {
      announcementToUpdate.getMedias().removeAll(mediaList);
    }

    announcementToUpdate.setAnnouncementTitle(announcementRequest.getAnnouncementTitle());
    announcementToUpdate
        .setAnnouncementDescription(announcementRequest.getAnnouncementDescription());
    announcementToUpdate.setSkill(skillMapper.toSkill(announcementRequest.getSkill()));
    announcementToUpdate
        .setAnnouncementSkillArea(skillAreaMapper.toSkillArea(announcementRequest.getSkillArea()));
    announcementToUpdate
        .setAnnouncementEndAvailableDate(announcementRequest.getAnnouncementEndAvailableDate());
    announcementToUpdate.setAnnouncementType(announcementRequest.getAnnouncementType());
    announcementToUpdate.setInterventionType(announcementRequest.getInterventionType());
    announcementToUpdate.setHoursNumber(announcementRequest.getHoursNumber());
    announcementToUpdate.setHomeService(announcementRequest.isHomeService());
    announcementToUpdate.setHomeServiceDetails(announcementRequest.getHomeServiceDetails());
    announcementToUpdate.setEstimatedPriceToPay(AnnouncementUtils.getEstimatedPriceToPay(announcementRequest.getAnnouncementType(), skillAreaMapper.toSkillArea(announcementRequest.getSkillArea())));
    announcementToUpdate.setEstimatedHourNumber(AnnouncementUtils.getEstimatedHours(announcementRequest.getAnnouncementType()));
    Announcement announcement = announcementRepository.save(announcementToUpdate);
    if (!mediaList.isEmpty()) {
      mediaList.forEach(media -> {
        mediaService.deleteMedia(media.getId());
        dBFileStorageService.deleteFile(media.getMediaLabel());
      });

    }
    return announcement;
  }

  public List<Media> checkAnouncementMedia(UUID announcementUuid, List<MediaDetails> newMediaList) {
    Announcement announcementToUpdate = getAnnouncementByUUID(announcementUuid);
    List<Media> mediaList = new ArrayList<>();
    if (announcementToUpdate != null) {
      AnnouncementDto p = announcementMapper.toAnnouncementDto(announcementToUpdate);
      if (!announcementToUpdate.getMedias().isEmpty()) {
        mediaList = p.getMedias().stream().filter(media -> !newMediaList.contains(media))
            .collect(Collectors.toList()).stream().map(media -> mediaDatailsMapper.toMedia(media))
            .collect(Collectors.toList());
      }
    }
    return mediaList;

  }

  public void deleteAnnouncement(UUID announcementUuid) {
    Announcement announcement = announcementRepository.findByUuid(announcementUuid);
    if (!announcement.getAnnouncementPublisher().getUuid()
        .equals(userService.getCurrentUser().getUuid())) {
      throw new UserForbiddenException("Cannot delete Announceent for other user");
    } else {
      announcement.setAnnouncementStatus(AnnouncementStatus.DELETED);
      announcementRepository.save(announcement);
    }

  }


  @Transactional
  public PageDto<AnnouncementFiltredResponseDto> getAnnouncementPaginated(
      Integer pageIndex,
      Integer offset,
      List<String> skillLevels,
      List<String> skills,
      List<String> interventionTypes,
      List<String> announcementTypes,
      String endAvailableDate

  ) {
    log.info("Filter Announcements for Home page");

    AnnouncementSearchCriteria announcementSearchCriteria =
        new AnnouncementSearchCriteria(skillLevels, skills, interventionTypes, announcementTypes,
            endAvailableDate);
    if (pageIndex <= 0) {
      throw new BadRequestException("page Index should be greater or equals than 1");
    }

    long totalElementsSize = 0l;
    List<Announcement> announcements = null;
    if (announcementSearchCriteria.isEmptyAnnouncementType() && announcementSearchCriteria
        .isEmptySkills() && announcementSearchCriteria.isEmptySkillLevels()
        && announcementSearchCriteria.isEmptyInterventionTypes()) {
      Pageable pageable = PageRequest.of(pageIndex - 1, offset, Sort.by("createdAt").ascending());
      Page<Announcement> announcementsPage =
          announcementRepository.findAll(AnnouncementSpecifications
              .createAnnouncementSpecifications(announcementSearchCriteria), pageable);

      totalElementsSize = announcementsPage.getTotalElements();
      announcements = announcementsPage.getContent();
    } else {
      announcements =
          announcementRepository.findAll(
              AnnouncementSpecifications
                  .createAnnouncementSpecifications(announcementSearchCriteria));

      totalElementsSize = announcements.size();
    }

    List<AnnouncementFiltredResponseDto> finalList =
        announcements
            .stream()
            .map(
                announcement -> {
                  AnnouncementFiltredResponseDto dto = new AnnouncementFiltredResponseDto();
                  dto.setAnnouncementUuid(announcement.getUuid());
                  dto.setAnnouncementTitle(announcement.getAnnouncementTitle());
                  dto.setAnnouncementDescription(announcement.getAnnouncementDescription());
                  dto.setAnnouncementEndAvailableDate(
                      announcement.getAnnouncementEndAvailableDate());
                  dto.setSkillLevel(announcement.getAnnouncementSkillArea().getSkillAreaCode());
                  dto.setAnnouncementIntervetionType(announcement.getInterventionType().toString());
                  dto.setAnnouncementType(announcement.getAnnouncementType().toString());
                  dto.setSkill(announcement.getSkill().getSkillCode());
                  dto.setAnnouncementPublisher(
                      userMapper.toUserResponse(announcement.getAnnouncementPublisher()));
                  dto.setAnnouncementMedias(announcement.getMedias().stream()
                      .map(media -> mediaDatailsMapper.toMediaDetails(media))
                      .collect(Collectors.toList()));
                  dto.setHomeService(announcement.isHomeService());
                  dto.setInStudyPackage(announcement.isInStudyPackage());
                  dto.setEstimatedPriceToPay(announcement.getEstimatedPriceToPay());
                  return dto;
                })
            .collect(Collectors.toList());

    return new PageDto<>(finalList, totalElementsSize);
  }

  public PageDto<AnnouncementFiltredResponseDto> getSimilarAnnouncementPaginated(
      AnnouncementDto announcementRequest

  ) {
    log.info("Filter Similar Announcements for Detail page");
    Announcement announcementDetails = announcementMapper.toAnnouncement(announcementRequest);
    long totalElementsSize = 0l;
    int offset = 5;
    List<Announcement> announcements = new ArrayList<>();

    Pageable pageable = PageRequest.of(0, offset, Sort.by("createdAt").ascending());
    SimilarAnnouncementSearchCriteria announcementSearchCriteria =
        new SimilarAnnouncementSearchCriteria(announcementDetails.getSkill().getSkillLabel(),
            "skill", announcementDetails.getUuid());
    Page<Announcement> announcementsPage =
        announcementRepository.findAll(AnnouncementSpecifications
            .createSimilarAnnouncementSpecifications(announcementSearchCriteria), pageable);
    totalElementsSize = announcementsPage.getTotalElements();
    announcements = announcementsPage.getContent().stream()
        .filter(announcement -> !announcement.getUuid().equals(announcementDetails.getUuid()))
        .collect(Collectors.toList());

    if (totalElementsSize < offset) {
      pageable = PageRequest.of(0, offset - announcements.size(), Sort.by("createdAt").ascending());
      announcementSearchCriteria =
          new SimilarAnnouncementSearchCriteria(
              announcementDetails.getAnnouncementSkillArea().getSkillAreaLabel(), "skillArea",
              announcementDetails.getUuid());

      announcementsPage =
          announcementRepository.findAll(AnnouncementSpecifications
              .createSimilarAnnouncementSpecifications(announcementSearchCriteria), pageable);

      announcements.addAll(announcementsPage.getContent().stream()
          .filter(announcement -> !announcement.getUuid().equals(announcementDetails.getUuid()))
          .collect(Collectors.toList()));


    }
    List<AnnouncementFiltredResponseDto> finalList =
        announcements
            .stream()
            .map(
                announcement -> {
                  AnnouncementFiltredResponseDto dto = new AnnouncementFiltredResponseDto();
                  dto.setAnnouncementUuid(announcement.getUuid());
                  dto.setAnnouncementTitle(announcement.getAnnouncementTitle());
                  dto.setAnnouncementDescription(announcement.getAnnouncementDescription());
                  dto.setAnnouncementEndAvailableDate(
                      announcement.getAnnouncementEndAvailableDate());
                  dto.setSkillLevel(announcement.getAnnouncementSkillArea().getSkillAreaLabel());
                  dto.setAnnouncementIntervetionType(announcement.getInterventionType().toString());
                  dto.setAnnouncementType(announcement.getAnnouncementType().toString());
                  dto.setSkill(announcement.getSkill().getSkillLabel());
                  dto.setAnnouncementPublisher(
                      userMapper.toUserResponse(announcement.getAnnouncementPublisher()));
                  dto.setAnnouncementMedias(announcement.getMedias().stream()
                      .map(media -> mediaDatailsMapper.toMediaDetails(media))
                      .collect(Collectors.toList()));
                  return dto;
                })
            .collect(Collectors.toList());

    return new PageDto<>(finalList, totalElementsSize);
  }

  public void updateAnnouncementStatus(Announcement announcement,AnnouncementStatus announcementStatus) {
    announcement.setAnnouncementStatus(announcementStatus);
    this.save(announcement);

  }

  public void sendNotificationEmail(Map<String, Object> maps,List<String> destinations,EmailContext emailContext) {

    EmailDto emailDto =
        new EmailDto();
    if (emailContext == EmailContext.NOTIF_EXPERT_PUB_ANNOUNCEMENT) {
      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_EXPERT_NEW_ANNOUNCEMENT + "[ " + maps.get("skill") + "/" + maps.get("skillArea") + " ]" , "notif-expert-pub-announcement.html", maps,
              new HashMap<>(),EmailContext.NOTIF_EXPERT_PUB_ANNOUNCEMENT);
    }
    else if (emailContext == EmailContext.NOTIF_USER_PUB_ANNOUNCEMENT) {
      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_USER_PUBLISHED_ANNOUNCEMENT, "notif-user-pub-announcement.html", maps,
              new HashMap<>(),EmailContext.NOTIF_USER_PUB_ANNOUNCEMENT);
    }
    else if(emailContext == EmailContext.NOTIF_USER_ANNOUNCEMENT_REFUSED){
      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_USER_ANNOUNCEMENT_REFUSED, "notif-user-announcement-refused.html", maps,
              new HashMap<>(),EmailContext.NOTIF_USER_ANNOUNCEMENT_REFUSED);

    }

    emailService.sendMail(emailDto,destinations);
  }

  public PageDto<AnnouncementDto> recoverannouncementList(AnnouncementStatus announcementStatus, Integer page, Integer offset) {

    int pageindex = page <= 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(pageindex, offset, Sort.by("createdAt").ascending());
    Page<Announcement> data;
    List<AnnouncementDto> result = new ArrayList<>();
    if (announcementStatus != null)
      data = announcementRepository.findByAnnouncementStatus(announcementStatus,pageable);
    else
      data = announcementRepository.findAll(pageable);
    if(data.hasContent())
    data.stream().forEach( annoncment ->{
      result.add(announcementMapper.toAnnouncementDto(annoncment));
    });
    return new PageDto<>(result, Long.valueOf(result.size()));
  }
  public void validate(UUID announcementUuid) {
    Announcement announcement = getAnnouncementByUUID(announcementUuid);
    announcement.setAnnouncementStatus(AnnouncementStatus.PUBLISHED);
    announcementRepository.save(announcement);
    // send email to expert
   /* List<String> destination = informationGiverRepository.findBySkillsId(announcement.getSkill().getId()).stream()
            .map(User::getUserEmail).collect(Collectors.toList());*/
    List<String> destination = informationGiverRepository.findByAccountStatus(AccountStatus.ACTIVE).stream()
        .map(User::getUserEmail).collect(Collectors.toList());
    Map<String, Object> maps = new HashMap<>();
    maps.put("announcementUuid", announcement.getUuid().toString());
    maps.put("title", announcement.getAnnouncementTitle());
    maps.put("skill", announcement.getSkill().getSkillLabel());
    maps.put("skillArea", announcement.getAnnouncementSkillArea().getSkillAreaLabel());
    if (destination != null && !destination.isEmpty()) {
      sendNotificationEmail(maps, destination,EmailContext.NOTIF_EXPERT_PUB_ANNOUNCEMENT);
    }

    // send email to publisher
    destination = Arrays.asList(announcement.getAnnouncementPublisher().getUserEmail());
    maps = new HashMap<>();
    maps.put("announcementUuid", announcement.getUuid().toString());
    maps.put("announcementTitle", announcement.getAnnouncementTitle());

    if (destination != null && !destination.isEmpty()) {
      sendNotificationEmail(maps, destination,EmailContext.NOTIF_USER_PUB_ANNOUNCEMENT);
    }
  }


  public void refuse(UUID uuid,MessageRequestDto messageRequest) {
    Announcement announcement = getAnnouncementByUUID(uuid);
    announcement.setAnnouncementStatus(AnnouncementStatus.REFUSED);
    announcement.setRefusalReason(messageRequest.getMessage());
    announcementRepository.save(announcement);
    // send email
    List<String> destination = Arrays.asList(announcement.getAnnouncementPublisher().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("announcementUuid", announcement.getUuid().toString());
    maps.put("refusalReason", messageRequest.getMessage());
    if (destination != null && !destination.isEmpty()) {
      sendNotificationEmail(maps, destination,EmailContext.NOTIF_USER_ANNOUNCEMENT_REFUSED);
    }
  }


  public List<Announcement> getPastAnnouncement(){
    return  announcementRepository.findByAnnouncementStatusNotAndAnnouncementEndAvailableDateBefore(AnnouncementStatus.FINISHED,DatesUtils.getMeYesterday());
  }
}


