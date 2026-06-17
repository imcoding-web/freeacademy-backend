package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.PackageSubscriptionService;
import fr.imcoding.edu365.client.dtos.request.PackageSubscriptionRequest;
import fr.imcoding.edu365.client.dtos.response.PackageSubscriptionResponse;
import fr.imcoding.edu365.dtos.SkillAreaWithPackagesDTO;
import fr.imcoding.edu365.enumeration.PackageType;
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


  @GetMapping
  public List<PackageSubscriptionResponse> getPackagesSubscription(@RequestParam(name="package-type")
      PackageType packageType) {
    return packageSubscriptionService.getAllPackageSubscription(packageType);
  }

  @PostMapping()
  public void savePackage(@RequestBody
      PackageSubscriptionRequest subscriptionRequest) {
    this.packageSubscriptionService.addPackageSubscription(subscriptionRequest);

  }

  @GetMapping("/finish-subscription")
  public void finishSubscription(@RequestParam(name="uuid") UUID uuid) {
     packageSubscriptionService.finishSubscription(uuid);
  }

  @GetMapping("/is-user-subscribed")
  public boolean checkUserSubscription() {
    return packageSubscriptionService.isStudentSubscribed();
  }
}
