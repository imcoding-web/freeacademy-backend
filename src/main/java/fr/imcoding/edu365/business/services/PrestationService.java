package fr.imcoding.edu365.business.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.imcoding.edu365.business.ext.zoom.response.ZoomMeetingObjectDTO;
import fr.imcoding.edu365.business.mappers.MediaMapper;
import fr.imcoding.edu365.business.mappers.PrestationMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.business.services.files.IFileService;
import fr.imcoding.edu365.business.services.zoomMeeting.ZoomMeetingService;
import fr.imcoding.edu365.client.dtos.request.PrestationAdminRequest;
import fr.imcoding.edu365.client.dtos.request.PrestationRequest;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.PrestationResponse;
import fr.imcoding.edu365.dtos.UserBankDataDto;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.InterventionType;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.enumeration.PrestationStatus;
import fr.imcoding.edu365.persistence.entities.Announcement;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.Prestation;
import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.AnnouncementRepository;
import fr.imcoding.edu365.persistence.repositories.InformationGiverRepository;
import fr.imcoding.edu365.persistence.repositories.InformationSeekerRepository;
import fr.imcoding.edu365.persistence.repositories.OfferRepository;
import fr.imcoding.edu365.persistence.repositories.PrestationRepository;
import fr.imcoding.edu365.utils.Constants;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */
@Service
@RequiredArgsConstructor
public class PrestationService {
  private final PrestationRepository prestationRepository;
  private final MediaService mediaService;
  private final UserService userService;
  private final PrestationMapper prestationMapper;
  private final IFileService dBFileStorageService;
  private final MediaMapper mediaMapper;
  private final ZoomMeetingService zoomMeetingService;
  private final PrestationMeetingService prestationMeetingService;
  private final UserBankDataService userBankDataService;
  private final EmailService emailService;
  private final InformationSeekerRepository informationSeekerRepository;
  private final InformationGiverRepository informationGiverRepository;
  private final SkillAreaService skillAreaService;
  private final SkillService skillService;
  private final AnnouncementRepository announcmentRepository;
  private final OfferRepository offerRepository;
  
  @Value("${edu365.payment.vat}")
  private Integer vatValue;



  public Prestation addPrestation(Prestation prestation){
    prestation.setPrestationStatus(PrestationStatus.IN_PROGRESS);

    prestation= prestationRepository.save(prestation);

    if(!prestation.getOffer().getAnnouncement().getInterventionType().equals(InterventionType.MAIL_CORRECTION)){
      ZoomMeetingObjectDTO meetingObjectDTO=zoomMeetingService.createMeeting(prestation.getOffer().getVideoconferenceDuration(),prestation.getOffer().getAnnouncement().getAnnouncementTitle());
      PrestationMeeting prestationMeeting=new PrestationMeeting();
      prestationMeeting.setPrestation(prestation);
      prestationMeeting.setMeetingJoinUrl(meetingObjectDTO.getJoin_url());
      prestationMeeting.setMeetingId(meetingObjectDTO.getId());
      prestationMeeting.setMeetingCodeSecret(meetingObjectDTO.getPassword());
      prestationMeetingService.addPrestationMeeting(prestationMeeting);
    }
    return prestation;
  }

  public List<PrestationResponse> getExpertPrestations(){
    InformationGiver user = (InformationGiver) userService.getCurrentUser();
    return prestationRepository.findByOfferOfferGiverUuid(user.getUuid()).stream().sorted(Comparator
        .comparing(Prestation::getCreatedAt).reversed()).map(prestationMapper::toPrestationResponse).collect(Collectors.toList());
  }

  public List<PrestationResponse> getUserPrestations(){
    InformationSeeker user = (InformationSeeker) userService.getCurrentUser();
    return prestationRepository.findByOfferAnnouncementAnnouncementPublisherUuid(user.getUuid()).stream().sorted(Comparator
        .comparing(Prestation::getCreatedAt).reversed()).map(prestationMapper::toPrestationResponse).collect(Collectors.toList());
  }

  public Prestation updatePrestation(PrestationRequest prestationRequest) {
    List<Media> mediaListToDelete = checkMediaPrestation(prestationRequest.getPrestationUuid(),
        prestationRequest.getMedias());
    List<Media> newMediaList = new ArrayList<>();
    Prestation prestationToUpdate = prestationMapper.toPrestation(prestationRequest);


    if (prestationRequest.getFiles() != null && !prestationRequest.getFiles().isEmpty()) {
      prestationRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.PRESTATION_DOCUMENT);
          newMediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      prestationToUpdate.getMedias().addAll(newMediaList);
    }

      prestationToUpdate.getMedias().removeAll(mediaListToDelete);
      Prestation prestation=prestationRepository.save(prestationToUpdate);
    if (!mediaListToDelete.isEmpty()) {
      mediaListToDelete.forEach(media -> {
        mediaService.deleteMedia(media.getId());
        dBFileStorageService.deleteFile(media);
      });


    }
    return prestation;

  }
  public Prestation markAsSolveld(UUID prestationUuid){
    Prestation prestation = prestationRepository.findByUuid(prestationUuid).orElse(null);
    prestation.setPrestationStatus(PrestationStatus.PENDING_VERIFICATION);
    prestation.setMarkAsSolvedDate(new Date());
    return prestationRepository.save(prestation);

  }

  public Prestation getByUuid(UUID uuid) {
    return prestationRepository.findByUuid(uuid).orElse(null);
  }
  public List<Media> checkMediaPrestation(UUID prestationUuid, List<MediaDto> newMediaList) {
    Prestation prestationToUpdate = getByUuid(prestationUuid);
    List<Media> mediaList = new ArrayList<>();
    if(newMediaList!=null &&!newMediaList.isEmpty()) {

        if (prestationToUpdate != null && !prestationToUpdate.getMedias().isEmpty()) {
          mediaList =prestationToUpdate.getMedias().stream().filter(media -> !newMediaList.contains(mediaMapper.toMediaDto(media)))
              .collect(Collectors.toList()).stream()
              .collect(Collectors.toList());
        }


    }
    return mediaList;
  }

  public List<PrestationResponse> getPrestations(PrestationStatus prestationStatus){
    return prestationRepository.findByPrestationStatus(prestationStatus).stream().map(prestationMapper::toPrestationResponse).collect(Collectors.toList());
  }


  @Transactional
  public void validatePrestation(UUID prestationUuid) {
    Prestation prestation = prestationRepository.findByUuid(prestationUuid).orElse(null);
    prestation.setPrestationStatus(PrestationStatus.RESOLVED);
    prestationRepository.save(prestation);

    userBankDataService.updateUserBankData(new UserBankDataDto(prestation.getOffer().getOfferGiver().getUuid(),prestation.getOffer().getOfferPrice()));

    // send email to expert
    List<String> destination = Arrays.asList(prestation.getOffer().getOfferGiver().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("announcementTitle", prestation.getOffer().getAnnouncement().getAnnouncementTitle());
    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_EXPERT_PRESTATION_VALID, "notif-expert-prestation-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_PRESTATION_VALID);

    emailService.sendMail(emailDto, destination);


  }

  public void refusePrestation(UUID prestationUuid,MessageRequestDto messageRequestDto) {
    Prestation prestation = prestationRepository.findByUuid(prestationUuid).orElse(null);
    prestation.setPrestationStatus(PrestationStatus.UNRESOLVED);
    prestation.setRefusalReason(messageRequestDto.getMessage());
    prestationRepository.save(prestation);

    // send email to expert
    List<String> destination = Arrays.asList(prestation.getOffer().getOfferGiver().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("announcementTitle", prestation.getOffer().getAnnouncement().getAnnouncementTitle());
    maps.put("refusalReason", prestation.getRefusalReason());

    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_EXPERT_PRESTATION_NOT_VALID, "notif-expert-prestation-not-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_PRESTATION_NOT_VALID);

    emailService.sendMail(emailDto, destination);


  }

  public PrestationResponse getPrestationDetails(UUID uuid) {
    return prestationMapper.toPrestationDetailsResponse(getByUuid(uuid));
  }


  public void notifyUserStartMeet(UUID prestationUuid) {
    Prestation prestation=getByUuid(prestationUuid);
    PrestationMeeting prestationMeeting=prestationMeetingService.getPrestationMeetingByPrestationUuid(prestation.getUuid());
    User user=prestation.getOffer().getAnnouncement().getAnnouncementPublisher();
    List<String> destination = Arrays.asList(user.getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("expertFullName", user.getUserLastName() + " " + user.getUserFirstName());
    maps.put("meetUrl", prestationMeeting.getMeetingJoinUrl());
    maps.put("meetId", prestationMeeting.getMeetingId());
    maps.put("meetSecretCode", prestationMeeting.getMeetingCodeSecret());
    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_USER_START_MEET, "notif-user-start-meet.html", maps,
            new HashMap<>(),EmailContext.NOTIF_USER_START_MEET);

    emailService.sendMail(emailDto, destination);

  }

	public void addPrestation(PrestationAdminRequest prestationRequest) {
		Announcement announcement = new Announcement();
		announcement.setAnnouncementTitle(prestationRequest.getSubject());
		announcement.setAnnouncementType(prestationRequest.getType());
		announcement.setInterventionType(prestationRequest.getInterventionType());
		announcement.setAnnouncementDescription(prestationRequest.getDescription());
		InformationSeeker user = informationSeekerRepository.findByUserEmail(prestationRequest.getStudentEmail()).get();
		announcement.setAnnouncementPublisher(user);
		announcement.setAnnouncementSkillArea(skillAreaService.findByCode(prestationRequest.getSkillAreaCode()));
		announcement.setSkill(skillService.findByCode(prestationRequest.getSkillCode()));
		announcement.setAnnouncementStatus(AnnouncementStatus.ASSIGNED_TO_TEACHER);
		announcement.setEstimatedHourNumber(prestationRequest.getEstimatedHourNumber());
		announcement = announcmentRepository.save(announcement);

		List<Media> mediaList = new ArrayList<>();
		prestationRequest.getFiles().stream().forEach(file -> {
			Media media;
			try {
				media = mediaService.saveMedia(file, MediaContext.ANNOUNCEMENT);
				mediaList.add(media);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});

		announcement.setMedias(mediaList);

		announcmentRepository.save(announcement);
		
		Offer offer = new Offer();
		offer.setAnnouncement(announcement);
		offer.setOfferStatus(OfferStatus.VALIDATED_AND_PAID);
		InformationGiver expert = informationGiverRepository.findByUserEmail(prestationRequest.getTeacherEmail()).get();
		offer.setOfferGiver(expert);
	    offer.setOfferPrice(prestationRequest.getPrice());
	    offer.setOfferVatPrice(prestationRequest.getPrice()+(prestationRequest.getPrice()*vatValue/100));
	    offer = offerRepository.save(offer);
	    
	    //envoyer des emails de notification pour à l'enseignant
	    Map<String, Object> maps = new HashMap<>();
	    List<String> destinations = Stream.of(expert.getUserEmail())
	        .collect(Collectors.toList());
	    maps.put("announcementUuid", announcement.getUuid().toString());
	    maps.put("announcementTitle", announcement.getAnnouncementTitle());
	    maps.put("userFullName", user.getUserLastName() + " " + user.getUserFirstName());
	    maps.put("userEmail", user.getUserEmail());
	    maps.put("userPhoneNumber", user.getUserPhoneNumber());

	    sendNotificationEmail(maps, destinations, EmailContext.NOTIF_EXPERT_ASSIGNMENT_TO_COURSE);
	    
	    //souvgarder la prestation et construire la réunion zoom
	    Prestation prestation=new Prestation();
	    prestation.setOffer(offer);
	    addPrestation(prestation);
	    
	  //envoyer des emails de notification pour à l'éleve / etudiant
	    Map<String, Object> mapsS = new HashMap<>();
	    List<String> destinationsS = Stream.of(expert.getUserEmail())
	        .collect(Collectors.toList());
	    maps.put("announcementUuid", announcement.getUuid().toString());
	    maps.put("announcementTitle", announcement.getAnnouncementTitle());
	    maps.put("expertFullName", expert.getUserLastName() + " " + expert.getUserFirstName());
	    maps.put("expertEmail", expert.getUserEmail());
	    maps.put("expertPhoneNumber", expert.getUserPhoneNumber());

	    sendNotificationEmail(maps, destinations, EmailContext.NOTIF_USER_ASSIGNMENT_TO_COURSE);

	}
	
	public void sendNotificationEmail(Map<String, Object> maps, List<String> destinations, EmailContext emailContext) {
		EmailDto emailDto = new EmailDto();
		if (emailContext == EmailContext.NOTIF_EXPERT_ASSIGNMENT_TO_COURSE) {
			emailDto = new EmailDto(Constants.MAIL_SUBJECT_EXPERT_ASSIGNMENT_TO_COURSE,
					"notif-expert-assignment-to-course", maps, new HashMap<>(),
					EmailContext.NOTIF_EXPERT_ASSIGNMENT_TO_COURSE);
		} else if (emailContext == EmailContext.NOTIF_USER_ASSIGNMENT_TO_COURSE) {
			emailDto = new EmailDto(Constants.MAIL_SUBJECT_USER_ASSIGNMENT_TO_COURSE,
					"notif-user-assignement-to-course.html", maps, new HashMap<>(),
					EmailContext.NOTIF_USER_ASSIGNMENT_TO_COURSE);
		}
		emailService.sendMail(emailDto, destinations);
	}

}
