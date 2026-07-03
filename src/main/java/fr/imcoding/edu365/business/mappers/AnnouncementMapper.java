package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.client.dtos.response.AnnouncementDetailsResponse;
import fr.imcoding.edu365.client.dtos.response.AnnouncementResponse;
import fr.imcoding.edu365.client.dtos.response.MyAnnouncementResponse;
import fr.imcoding.edu365.client.dtos.response.OfferResponse;
import fr.imcoding.edu365.dtos.AnnouncementDto;
import fr.imcoding.edu365.persistence.entities.Announcement;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.repositories.AnnouncementRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnouncementMapper {
  private final AnnouncementRepository announcementRepository;

  private final UserMapper userMapper;
  private final SkillAreaMapper skillAreaMapper;
  private final SkillMapper skillMapper;
  private final MediaMapper mediaMapper;
  private final MediaDatailsMapper mediaDatailsMapper;
 // private final OfferService offerService;


  public Announcement toAnnouncement(AnnouncementDto announcementRequest){
    return announcementRepository.findByUuid(announcementRequest.getAnnouncementUuid());
  }
  public AnnouncementResponse toAnnouncementResponse(Announcement announcement) {
    return new AnnouncementResponse(announcement.getUuid(),
        announcement.getAnnouncementTitle(),
        announcement.getCreatedAt(),
        announcement.getAnnouncementNumberLike(),
        announcement.getAnnouncementNumberDislike(),
        announcement.getAnnouncementType(),
        userMapper.toUserDto(announcement.getAnnouncementPublisher()),
        !announcement.getMedias().isEmpty() ? announcement.getMedias().get(0).getMediaUrl() : null);
  }

  public AnnouncementDetailsResponse toAnnouncementDetailsResponse(Announcement announcement) {

    List<String> listofPictures = announcement.getMedias().stream().map(Media::getMediaUrl)
        .collect(Collectors.toList());

    return new AnnouncementDetailsResponse(announcement.getUuid(),
        announcement.getAnnouncementTitle(),
        announcement.getAnnouncementDescription(),
        announcement.getCreatedAt(),
        announcement.getAnnouncementSummary(),
        announcement.getAnnouncementAdditionalInformations(),
        announcement.getAnnouncementType(),
        userMapper.toUserDto(announcement.getAnnouncementPublisher()),
        listofPictures,
        announcement.getAnnouncementEndAvailableDate());
  }

  public MyAnnouncementResponse toMyAnnouncementResponse(Announcement announcement) {
//    List<OfferResponse> offers=offerService.getAnnouncementOffers(announcement.getUuid());
    return new MyAnnouncementResponse(
        announcement.getUuid(),
        announcement.getAnnouncementTitle(),
        announcement.getCreatedAt(),
        announcement.getAnnouncementNumberLike(),
        announcement.getAnnouncementNumberDislike(),
        announcement.getAnnouncementType(),
        userMapper.toUserDto(announcement.getAnnouncementPublisher()),
        !announcement.getMedias().isEmpty() ? announcement.getMedias().get(0).getMediaUrl() : null,
        announcement.getAnnouncementStatus(),
        announcement.getAnnouncementEndAvailableDate(),
        skillAreaMapper.toSkillAreaDto(announcement.getAnnouncementSkillArea()),
        skillMapper.toSkillDto(announcement.getSkill()),
        announcement.getInterventionType(),
        announcement.getMedias().stream().map(media->mediaMapper.toMediaDto(media)).collect(
            Collectors.toList()),0

    );
    //stream().map(offer->offerMapper.toOfferResponse(offer)).collect(Collectors.toList()));
    // !offers.isEmpty()?offers.stream().map(offer->offerMapper.toOfferResponse(offer)).collect(Collectors.toList()));


  }

  public AnnouncementDto toAnnouncementDto(Announcement announcement){
    return new AnnouncementDto(
        announcement.getUuid(),
        announcement.getAnnouncementDescription(),
        announcement.getAnnouncementTitle(),
        announcement.getAnnouncementType(),
        announcement.getAnnouncementEndAvailableDate(),
        announcement.getCreatedAt(),
        skillMapper.toSkillDto(announcement.getSkill()),
        skillAreaMapper.toSkillAreaDto(announcement.getAnnouncementSkillArea()),
        announcement.getInterventionType(),
        announcement.getMedias().stream().map(media->mediaDatailsMapper.toMediaDetails(media)).collect(
            Collectors.toList()),
        userMapper.toUserResponse(announcement.getAnnouncementPublisher()),
        announcement.getHoursNumber(),announcement.getAnnouncementStatus(),
        announcement.isHomeService(),
        announcement.getHomeServiceDetails(),
        announcement.getEstimatedHourNumber()

    );

  }

}