package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.AnnouncementService;
import fr.imcoding.edu365.client.dtos.request.AnnouncementRequest;
import fr.imcoding.edu365.client.dtos.response.AnnouncementFiltredResponseDto;
import fr.imcoding.edu365.client.dtos.response.AnnouncementResponse;
import fr.imcoding.edu365.client.dtos.response.MyAnnouncementResponse;
import fr.imcoding.edu365.dtos.AnnouncementDto;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.persistence.entities.Announcement;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

  private final AnnouncementService announcementService;

/*
  @GetMapping()
  public List<Announcement> getAllAnnouncements() {
    return this.announcementService.getAllAnnouncements();
  }
*/

  @GetMapping("/details/{uuid}")
  public AnnouncementDto getAnnouncementDetailsById(
      @PathVariable(name = "uuid") UUID announcmeentuuid) {
    return this.announcementService.getAnnouncementDetailsByUUID(announcmeentuuid);
  }


  @PostMapping()
  public Announcement saveAnnouncement(@RequestBody AnnouncementRequest announcement) {
    return this.announcementService.saveAnnouncement(announcement);
  }

  @GetMapping("/last-published")
  public List<AnnouncementResponse> findLastAnnouncements() {
    return this.announcementService.findLastAnnouncements();
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_SEEKER'})")
  @GetMapping("/my-announcements")
  public List<MyAnnouncementResponse> findMyAnnouncements() {
    return this.announcementService.getMyAnnoucements();
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_SEEKER'})")
  @PatchMapping("/update-announcement")
  public Announcement updateAnnouncement(@RequestBody AnnouncementDto announcementRequest) {
    return this.announcementService.updateAnnouncement(announcementRequest);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_SEEKER'})")
  @DeleteMapping("/{announcement-uuid}")
  public void deleteAnnouncement(
      @PathVariable(name = "announcement-uuid") UUID announcementUuid) {
    announcementService.deleteAnnouncement(announcementUuid);
  }

  @GetMapping(value = "/filtred")
  public PageDto<AnnouncementFiltredResponseDto> getAnnouncementsPaginated(
      @RequestParam(name = "page") Integer page,
      @RequestParam(name = "offset") Integer offset,
      @RequestParam(name = "skill-level", required = false)
          List<String> skillLevels,
      @RequestParam(name = "skill", required = false)
          List<String> skills,
      @RequestParam(name = "intervention-type", required = false)
          List<String> interventionTypes,
      @RequestParam(name = "announcement-type", required = false)
          List<String> announcementTypes,
      @RequestParam(name = "end-available-date", required = false)
          String endAvailableDate) {

    return announcementService.getAnnouncementPaginated(
        page, offset, skillLevels, skills,interventionTypes,announcementTypes,endAvailableDate);
  }
  @PostMapping(value = "/similar")
  public PageDto<AnnouncementFiltredResponseDto> getAnnouncementsPaginated(
      @RequestBody AnnouncementDto announcement) {
    return announcementService.getSimilarAnnouncementPaginated(
        announcement);
  }

  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping()
  public  PageDto<AnnouncementDto> recoverannouncementList(
          @RequestParam(value = "announcement-status", required = false) AnnouncementStatus announcementStatus,
          @RequestParam(name = "page") Integer page,
          @RequestParam(name = "offset") Integer offset) {
    return announcementService.recoverannouncementList(announcementStatus,page,offset);
  }
  @GetMapping(value = "/validate")
  public void validate(@RequestParam("announcement-uuid") UUID announcementUuid) {
    announcementService.validate(announcementUuid);
  }

  @PutMapping(value = "/refuse")
  public void refuse(@RequestParam("announcement-uuid") UUID announcementUuid,@RequestBody
      MessageRequestDto messageRequestDto) {
      announcementService.refuse(announcementUuid,messageRequestDto);
  }
}
