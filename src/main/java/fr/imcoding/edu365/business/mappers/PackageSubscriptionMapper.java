package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.client.dtos.response.PackageSubscriptionResponse;
import fr.imcoding.edu365.persistence.entities.PackageSubscription;
import fr.imcoding.edu365.persistence.entities.SkillSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
@Component
@RequiredArgsConstructor
public class PackageSubscriptionMapper {
 private final UserMapper userMapper;

 public PackageSubscriptionResponse toPackageSubscriptionResponse(PackageSubscription packageSubscription){
   return PackageSubscriptionResponse.builder().uuid(packageSubscription.getUuid())
       .startDate(packageSubscription.getStartDate())
       .endDate(packageSubscription.getEndDate())
       .packageType(packageSubscription.getSkillAreaPackage().getPackageType()).subscriptionStatus(packageSubscription.getSubscriptionStatus())
       .student(userMapper.toStudentResponse(packageSubscription.getStudent())).build();
 }

  public PackageSubscriptionResponse toPackageSubscriptionResponse(
      SkillSubscription packageSubscription){
    return PackageSubscriptionResponse.builder().uuid(packageSubscription.getUuid())
        .startDate(packageSubscription.getStartDate())
        .endDate(packageSubscription.getEndDate())
        .packageType(packageSubscription.getPackageType()).subscriptionStatus(packageSubscription.getSubscriptionStatus())
        .student(userMapper.toStudentResponse(packageSubscription.getStudent())).build();
  }

}
