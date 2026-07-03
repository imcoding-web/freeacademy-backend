package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.PackageSubscriptionService;
import fr.imcoding.edu365.business.services.SkillSubscriptionService;
import fr.imcoding.edu365.client.dtos.request.PackageSubscriptionRequest;
import fr.imcoding.edu365.client.dtos.response.PackageSubscriptionResponse;
import fr.imcoding.edu365.dtos.SkillAreaWithPackagesDTO;
import fr.imcoding.edu365.enumeration.PackageType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */

@RestController
@RequestMapping("/package-subscription")
@CrossOrigin
@RequiredArgsConstructor
public class PackageSubscriptionController {
  private final PackageSubscriptionService packageSubscriptionService;
  private final SkillSubscriptionService skillSubscriptionService;


  @GetMapping
  public List<PackageSubscriptionResponse> getPackagesSubscription(@RequestParam(name="package-type")
      PackageType packageType) {
    List<PackageSubscriptionResponse> response = new ArrayList<>();
    response.addAll(packageSubscriptionService.getAllPackageSubscription(packageType));
    response.addAll(skillSubscriptionService.getAllSkillSubscription(packageType));
    return response;
  }

  @PostMapping()
  public void savePackage(@RequestBody
      PackageSubscriptionRequest subscriptionRequest) {
    if(subscriptionRequest.getSkillCode() == null) {
      this.packageSubscriptionService.addPackageSubscription(subscriptionRequest);
    } else {
      skillSubscriptionService.addSkillSubscription(subscriptionRequest);
    }


  }

  @GetMapping("/finish-subscription")
  public void finishSubscription(@RequestParam(name="uuid") UUID uuid) {
    // on fait els deux successivenement car si ca passe avec une ca doit passer avec une autre: les isncriptions sont destribués sur deux tables
     packageSubscriptionService.finishSubscription(uuid);
     skillSubscriptionService.finishSubscription(uuid);
  }

  @GetMapping("/is-user-subscribed")
  public boolean checkUserSubscription(@RequestParam("courseId") UUID cousreId) {
    boolean isUserSubscribedToAPack =  packageSubscriptionService.isStudentSubscribed();
    if(!isUserSubscribedToAPack) {
        isUserSubscribedToAPack = skillSubscriptionService.isUserSubscribedToACousreSkill(cousreId);
    }
    return isUserSubscribedToAPack;
  }
}
