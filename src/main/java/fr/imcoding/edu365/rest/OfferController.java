package fr.imcoding.edu365.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.PaymentService;
import fr.imcoding.edu365.client.dtos.request.AcceptOfferRequest;
import fr.imcoding.edu365.client.dtos.request.OfferRequest;
import fr.imcoding.edu365.client.dtos.response.HasOfferResponse;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.persistence.entities.Offer;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 21/08/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
  private final OfferService offerService;
  private final PaymentService paymentService;


  @PostMapping()
  public ResponseEntity<Void> createOffer(@RequestParam("announcement-uuid") UUID announcementUuid,
      @RequestBody OfferRequest offer) {
    this.offerService.saveOffer(offer, announcementUuid);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @GetMapping()
  @PreAuthorize("hasAnyAuthority({'INFORMATION_SEEKER'})")
  public List<OfferResponseDto> getOffersByAnnouncement(@RequestParam("announcement-uuid") UUID announcementUuid) {
    return this.offerService.getAnnouncementOffers(announcementUuid);
  }

  @GetMapping("/offer-by-uuid")
  public OfferResponseDto getOffersByUuid(@RequestParam("offer-uuid") UUID offerUuid) {
    return this.offerService.getOfferByUuid(offerUuid);
  }


  @PostMapping("/accept-offer")
  public ResponseEntity<Void> updateOffer(
      @RequestBody AcceptOfferRequest offer) {
    this.offerService.acceptOffer(offer);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  
  @PostMapping("/accept-offer-in-package")
  public ResponseEntity<Void> acceptOfferInPackage(
		  @RequestParam("offer-uuid") UUID offerUuid ) {
    this.offerService.acceptOfferInPackage(offerUuid);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/expert-offer")
  public List<OfferResponseDto> getAnnouncementsByExpert() {
    return offerService.getAnnouncementsByExpert();
  }

  @PutMapping(value = "/confirm-offer")
  public Offer confirmOffer(@RequestParam("offer-uuid") UUID offerUuid,@RequestBody(required=false) String wordForAdvertiser) {
    return offerService.confirmOffer(offerUuid,wordForAdvertiser);
  }

  @PutMapping(value = "/refuse-offer")
  public Offer refuseOffer(@RequestParam("offer-uuid") UUID offerUuid,@RequestBody(required=false) String refusingReason) {
    return offerService.refuseOffer(offerUuid,refusingReason);
  }

  @GetMapping(value = "/cancel-offer")
  public Offer cancelOffer(@RequestParam("offer-uuid") UUID offerUuid) {
    return offerService.cancelOffer(offerUuid);
  }

  @GetMapping(value = "/has-offer")
  public HasOfferResponse hasAnnouncementOffer(@RequestParam("announcement-uuid") UUID announcementUuid) {
    return offerService.hasOffer(announcementUuid);
  }

  @GetMapping(value = "/expert-offers")
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  public List<OfferResponseDto> getExpertOffers() {
    return offerService.getExpertOffers();
  }

  @GetMapping(value = "/user-offers")
  @PreAuthorize("hasAnyAuthority({'INFORMATION_SEEKER'})")
  public List<OfferResponseDto> getUserOffers() {
    return offerService.getClientOffers();
  }

  @GetMapping(value = "/expert-prestations")
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  public List<OfferResponseDto> getExpertPrestations() {
    return paymentService.getExpertPrestations();
  }

}
