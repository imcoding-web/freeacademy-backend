package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.InformationGiverService;
import fr.imcoding.edu365.client.dtos.request.ExpertDescriptionRequest;
import fr.imcoding.edu365.client.dtos.request.ExpertRegisterRequest;
import fr.imcoding.edu365.client.dtos.request.PositionRequest;
import fr.imcoding.edu365.client.dtos.request.UserBankDataRequest;
import fr.imcoding.edu365.client.dtos.response.ExpertResponse;
import fr.imcoding.edu365.client.dtos.response.PositionResponse;
import fr.imcoding.edu365.client.dtos.response.UserBankDataResponse;
import fr.imcoding.edu365.client.dtos.response.ValidationExpertResponse;
import fr.imcoding.edu365.dtos.*;
import fr.imcoding.edu365.enumeration.ValidationStatus;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author Rokaya
 * @Date 13/06/2022
 */
@RestController
@CrossOrigin(origins = "http://localhost:4201", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/expert")
@RequiredArgsConstructor
public class InformationGiverController {
  private final InformationGiverService informationGiverService;

  @PostMapping("/register")
  public ResponseEntity<Void> registerTeacher(@ModelAttribute ExpertRegisterRequest registerRequest) throws Exception {
	  if(!registerRequest.isTermsAccepted()) {
		  return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	  }
	  informationGiverService.registerTeacher(registerRequest);
	  return ResponseEntity.ok().build();
  }
  
  @PostMapping(value = "/verify-email")
  public Boolean checkUserEmailForTeacherRequest(@NotEmpty @Email @RequestBody EmailVerifyDto emailVerify) {
    return informationGiverService.checkUserEmailForTeacherRequest(emailVerify.getUserEmail());
  }
  
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping("/expert-info")
  public ExpertDetails getExpertInfo() {
    return this.informationGiverService.getExpertInfo();
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PatchMapping("/update-expert")
  public ExpertDetails patchUser(@RequestBody ExpertDetails userRequest) {
    return this.informationGiverService.patchUser(userRequest);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PostMapping("/update-position")
  public PositionResponse updateUserPosition(@RequestBody PositionRequest positionRequest) {
    return this.informationGiverService.updateUserPosition(positionRequest);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping(value = "/my-position")
  public PositionResponse getUserPosition() {
    return this.informationGiverService.getUserPosition();
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping(value = "/my-description")
  public ExpertDescription getUserDescription() {
    return this.informationGiverService.getExpertDescription();
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PatchMapping(value = "/update-description")
  public ExpertDescription updateDescription(@ModelAttribute ExpertDescriptionRequest expertDescription) {
    return this.informationGiverService.updateExpertIntro(expertDescription);
  }


  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping(value = "/my-bank-data")
  public UserBankDataResponse getUserBankData() {
    return this.informationGiverService.getUserBankData();
  }
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PostMapping("/update-bank-data")
  public UserBankDataResponse updateUserPosition(@RequestBody UserBankDataRequest bankDataDto) {
    return this.informationGiverService.updateUserBankData(bankDataDto);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping("/validate-account")
  public ValidationAccountResponse validateAccount() {
    return this.informationGiverService.checkFieldsIsNull();
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping("/validation-history")
  public List<ValidationHistoryDto> getValidationHistory(){
    return this.informationGiverService.getValidationHistory();
  }

  /**
   * * recover all validation list
   * @param validationStatus de type string thats not required (could be null)
   * @return  ValidationExpertResponse custom list with all user information
   */
  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping()
  public List<ValidationExpertResponse> recoverValidationList(@RequestParam(value = "validation-status", required = false) String validationStatus) {
    return  informationGiverService.recoverValidationList(validationStatus);

  }
  
  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping("/inscription-requests")
  public List<ValidationExpertResponse> recoverTeahcerInscriptionRequests() {
    return  informationGiverService.recoverTeahcerInscriptionRequests();

  }
  
  @GetMapping(value = "/register-request/{uuid}")
  public ValidationExpertResponse getTeacherRequest(@PathVariable("uuid") UUID registerRequestUuid) {
    return informationGiverService.getTeacherRequest(registerRequestUuid);
  }
  
  @PutMapping(value = "/validate-request/{uuid}")
  public void validateTeacherRequest(@PathVariable("uuid") UUID registerRequestUuid, @RequestParam("assigned-course") String assignedCourseCode) {
     informationGiverService.validateTeacherRequest(registerRequestUuid, assignedCourseCode);
  }

  @GetMapping(value = "/validate-expert")
  public void confirmValidation(@RequestParam("validation-uuid") UUID validationUuid) {
    informationGiverService.confirmValidation(validationUuid);
  }
  @PutMapping(value = "/refuse-validation")
  public void refuseValidation(@RequestParam("validation-uuid") UUID validationUuid,@RequestBody MessageRequestDto messageRequestDto) {
    informationGiverService.refuseValidation(validationUuid,messageRequestDto);
  }
  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping("/experts-list")
  public List<ExpertResponse> getAllExpert() {
    return  informationGiverService.getExpertList();

  }

  @GetMapping("/expert-profile")
  public ExpertProfileDto getExperProfile(@RequestParam("expert-uuid") UUID uuid){
    return informationGiverService.getExpertProfile(uuid);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping(value = "/expert-home-services")
  public ExpertHomeServiceDto getUserHomeServiceData() {
    return this.informationGiverService.getExpertHomeServiceData();
  }
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PatchMapping(value = "/update-home-services")
  public ExpertHomeServiceDto updateHomeServiceData(@RequestBody ExpertHomeServiceDto expertHomeServiceRequest) {
    return this.informationGiverService.updateExpertHomeService(expertHomeServiceRequest);
  }


  //@PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping("/teacher-details")
  public UserDetails getTeacherDetails(@RequestParam("teacher-uuid") UUID uuid) {
    return this.informationGiverService.getUserByUuid(uuid);
  }


}
