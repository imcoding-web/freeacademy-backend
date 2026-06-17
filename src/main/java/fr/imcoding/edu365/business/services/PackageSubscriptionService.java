package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.PackageSubscriptionMapper;
import fr.imcoding.edu365.client.dtos.request.PackageSubscriptionRequest;
import fr.imcoding.edu365.client.dtos.response.PackageSubscriptionResponse;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.PackageSubscription;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import fr.imcoding.edu365.utils.DatesUtils;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
@Service
@RequiredArgsConstructor
public class PackageSubscriptionService {
  private final UserService userService;
  private final PackageSubscriptionRepository subscriptionRepository;
  private final PackageSubscriptionMapper packageSubscriptionMapper;


  private final SkillAreaPackageService skillAreaPackageService;

  public PackageSubscription addPackageSubscription(PackageSubscriptionRequest subscriptionRequest){
    InformationSeeker student=(InformationSeeker)userService.getUserByUUID(subscriptionRequest.getStudentUuid());
    PackageSubscription packageSubscription=new PackageSubscription();
    packageSubscription.setStartDate(new Date());
    packageSubscription.setEndDate(DatesUtils.calculateEndDate(packageSubscription.getStartDate(),subscriptionRequest.getMonthsNumber()));
    packageSubscription.setSubscriptionStatus(PackageStatus.ACTIVE);
    packageSubscription.setStudent(student);
    if(student.getCurrentLevel()!=null) {
      packageSubscription.setSkillAreaPackage(skillAreaPackageService
          .getPackBySkillLevelAndType(student.getCurrentLevel().getUuid(),
              subscriptionRequest.getPackageType()));
    }

    return subscriptionRepository.save(packageSubscription);

  }


  public List<PackageSubscriptionResponse> getAllPackageSubscription(PackageType packageType){
    return this.subscriptionRepository.findBySkillAreaPackagePackageType(packageType).stream().map(packageSubscription -> packageSubscriptionMapper.toPackageSubscriptionResponse(packageSubscription)).collect(Collectors
        .toList());
  }

  public void finishSubscription(UUID uuid){
    PackageSubscription packageSubscription=subscriptionRepository.findByUuid(uuid).orElse(null);
    if(packageSubscription!=null){
      packageSubscription.setSubscriptionStatus(PackageStatus.FINISHED);
      subscriptionRepository.save(packageSubscription);
    }
  }

  public boolean isStudentSubscribed() {
    InformationSeeker student=(InformationSeeker)userService.getCurrentUser();
    return subscriptionRepository.existsByStudentAndSubscriptionStatus(student, PackageStatus.ACTIVE);
  }
}
