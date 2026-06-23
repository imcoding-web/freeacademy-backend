package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.imcoding.edu365.business.services.PrestationService;
import fr.imcoding.edu365.client.dtos.request.PrestationAdminRequest;
import fr.imcoding.edu365.client.dtos.request.PrestationRequest;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.PrestationResponse;
import fr.imcoding.edu365.enumeration.PrestationStatus;
import fr.imcoding.edu365.persistence.entities.Prestation;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/prestation")
@RequiredArgsConstructor
public class PrestationController {

  private final PrestationService prestationService;

  @GetMapping
  public  List<PrestationResponse> getExpertPrestation(){
    return prestationService.getExpertPrestations();
  }

  @GetMapping(value = "/user-prestations")
  public List<PrestationResponse> getUserPrestations(@RequestParam("show-finished") boolean showFinished){
    return prestationService.getUserPrestations(showFinished);
  }

  @GetMapping(value = "/{prestationId}")
  public PrestationResponse getPrestation(@PathVariable("prestationId") UUID prestationId){
    return prestationService.getPrestation(prestationId);
  }

  @PatchMapping(value="/update-prestation",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Prestation updatePrestation(@ModelAttribute PrestationRequest prestationRequest) {
    return this.prestationService.updatePrestation(prestationRequest);
  }

  @GetMapping(value="/mark-as-solved")
  public Prestation markAsSolved(@RequestParam(value="prestation-uuid") UUID prestationUuid) {
    return this.prestationService.markAsSolveld(prestationUuid);
  }

  @GetMapping(value = "/prestation-by-status")
  public List<PrestationResponse> getListPrestations(@RequestParam(value="prestation-status")PrestationStatus prestationStatus){
    return prestationService.getPrestations(prestationStatus);
  }

  @GetMapping(value = "/prestation-by-uuid")
  public PrestationResponse getPrestationByUuid(@RequestParam(value="prestation-uuid")UUID prestationUuid){
    return prestationService.getPrestationDetails(prestationUuid);
  }

  @GetMapping(value = "/notify-user-start-meet")
  public void notifyUserStartMeet(@RequestParam(value="prestation-uuid") UUID prestationUuid){
     prestationService.notifyUserStartMeet(prestationUuid);
  }


  @GetMapping(value="/validate")
  public void validatePrestation(@RequestParam(value="prestation-uuid") UUID prestationUuid) {
     prestationService.validatePrestation(prestationUuid);
  }

  @PutMapping(value = "/refuse")
  public void refuse(@RequestParam("prestation-uuid") UUID prestationUuid,
      @RequestBody MessageRequestDto messageRequestDto) {
    prestationService.refusePrestation(prestationUuid, messageRequestDto);
  }
  
  @PostMapping(value = "/add")
  public void addPrestation(@ModelAttribute PrestationAdminRequest prestation) {
    prestationService.addPrestation(prestation);
  }

  @PostMapping("/set-prestation-date/{uuid}")
  public ResponseEntity<Void> setTeacherCourseDate(@PathVariable(name = "uuid") UUID prestationId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
    prestationService.setPrestationDate(prestationId, date);
    return ResponseEntity.ok().build();
  }
  @GetMapping(value="/live")
  public List<PrestationMeeting> getLivePrestations() {
    return prestationService.getLivePrestations();
  }

}
