package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.PackageSubscriptionMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.request.PackageSubscriptionRequest;
import fr.imcoding.edu365.client.dtos.response.PackageSubscriptionResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.PackageSubscription;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import fr.imcoding.edu365.utils.Constants;
import fr.imcoding.edu365.utils.DatesUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  private final EmailService emailService;


  private final SkillAreaPackageService skillAreaPackageService;

  public PackageSubscription addPackageSubscription(PackageSubscriptionRequest subscriptionRequest){
    InformationSeeker student=(InformationSeeker)userService.getUserByUUID(subscriptionRequest.getStudentUuid());
    PackageSubscription packageSubscription=new PackageSubscription();
    packageSubscription.setStartDate(LocalDate.now());
    packageSubscription.setEndDate(DatesUtils.calculateEndDate(packageSubscription.getStartDate(),subscriptionRequest.getMonthsNumber()));
    packageSubscription.setSubscriptionStatus(PackageStatus.ACTIVE);
    packageSubscription.setStudent(student);
    if(student.getCurrentLevel()!=null) {
      packageSubscription.setSkillAreaPackage(skillAreaPackageService
          .getPackBySkillLevelAndType(student.getCurrentLevel().getUuid(),
              subscriptionRequest.getPackageType()));
    }

    Map<String, Object> maps = new HashMap<>();
    List<String> destinations = Stream.of(student.getUserEmail())
            .collect(Collectors.toList());
    maps.put("packType", subscriptionRequest.getPackageType().toString());
    maps.put("packLevel", student.getCurrentLevel().getSkillAreaLabel());
    maps.put("packSection", student.getCurrentLevelSection() != null ? student.getCurrentLevelSection().getLabel(): "Non Défini");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    maps.put("endDate", formatter.format(packageSubscription.getEndDate()));
    maps.put("userFullName", student.getUserLastName() + " " + student.getUserFirstName());
    sendNotificationEmail(maps, destinations, EmailContext.NOTIF_USER_PACK_SUBSCRIPTION);

    return subscriptionRepository.save(packageSubscription);

  }

  public void sendNotificationEmail(Map<String, Object> maps, List<String> destinations,
                                    EmailContext emailContext) {
    EmailDto emailDto =
            new EmailDto();
    if (emailContext == EmailContext.NOTIF_USER_PACK_SUBSCRIPTION) {
      emailDto =
              new EmailDto(
                      Constants.MAIL_SUBJECT_NOTIF_USER_PACK_SUBSCRIPTION, "notif-user-pack-subscription-sucess.html", maps,
                      new HashMap<>(),EmailContext.NOTIF_USER_PACK_SUBSCRIPTION);
    }
    emailService.sendMail(emailDto, destinations);
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
