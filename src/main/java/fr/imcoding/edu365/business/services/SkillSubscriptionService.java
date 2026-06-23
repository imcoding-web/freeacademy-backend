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
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import fr.imcoding.edu365.persistence.entities.SkillSubscription;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.repositories.PackageSubscriptionRepository;
import fr.imcoding.edu365.persistence.repositories.SkillRepository;
import fr.imcoding.edu365.persistence.repositories.SkillSubscriptionRepository;
import fr.imcoding.edu365.persistence.repositories.TeacherCourseRepository;
import fr.imcoding.edu365.utils.Constants;
import fr.imcoding.edu365.utils.DatesUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
//service pour gérer les inscriptions pour uns eul matiére (pas tout le pack)
@Service
@RequiredArgsConstructor
public class SkillSubscriptionService {
  private final UserService userService;
  private final SkillSubscriptionRepository subscriptionRepository;
  private final SkillRepository skillRepository;
  private final PackageSubscriptionMapper packageSubscriptionMapper;
  private final TeacherCourseRepository teacherCourseRepository;

  private final EmailService emailService;


  private final SkillAreaPackageService skillAreaPackageService;

  public SkillSubscription addSkillSubscription(PackageSubscriptionRequest subscriptionRequest){
    InformationSeeker student=(InformationSeeker)userService.getUserByUUID(subscriptionRequest.getStudentUuid());
    SkillSubscription skillSubscription=new SkillSubscription();
    skillSubscription.setStartDate(LocalDate.now());
    skillSubscription.setEndDate(DatesUtils.calculateEndDate(skillSubscription.getStartDate(),subscriptionRequest.getMonthsNumber()));
    skillSubscription.setSubscriptionStatus(PackageStatus.ACTIVE);
    skillSubscription.setStudent(student);
    if(student.getCurrentLevel()!=null) {
      skillSubscription.setSkillLevel(student.getCurrentLevel());
    }
    Skill skill = skillRepository.findBySkillCode(subscriptionRequest.getSkillCode());
    skillSubscription.setSkill(skill);
    skillSubscription.setPackageType(subscriptionRequest.getPackageType());

    Map<String, Object> maps = new HashMap<>();
    List<String> destinations = Stream.of(student.getUserEmail())
            .collect(Collectors.toList());
    maps.put("packType", subscriptionRequest.getPackageType().toString());
    maps.put("packLevel", student.getCurrentLevel().getSkillAreaLabel());
    maps.put("skillLabel", skill.getSkillLabel());
    maps.put("packSection", student.getCurrentLevelSection() != null ? student.getCurrentLevelSection().getLabel(): "Non Défini");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    maps.put("endDate", formatter.format(skillSubscription.getEndDate()));
    maps.put("userFullName", student.getUserLastName() + " " + student.getUserFirstName());
    sendNotificationEmail(maps, destinations, EmailContext.NOTIF_USER_SKILL_SUBSCRIPTION);

    return subscriptionRepository.save(skillSubscription);

  }

  public void sendNotificationEmail(Map<String, Object> maps, List<String> destinations,
                                    EmailContext emailContext) {
    EmailDto emailDto =
            new EmailDto();
    if (emailContext == EmailContext.NOTIF_USER_SKILL_SUBSCRIPTION) {
      emailDto =
              new EmailDto(
                      Constants.MAIL_SUBJECT_NOTIF_USER_PACK_SUBSCRIPTION, "notif-user-skill-subscription-sucess.html", maps,
                      new HashMap<>(),EmailContext.NOTIF_USER_SKILL_SUBSCRIPTION);
    }
    emailService.sendMail(emailDto, destinations);
  }




  public List<PackageSubscriptionResponse> getAllSkillSubscription(PackageType packageType){
    return this.subscriptionRepository.findByPackageType(packageType).stream().map(packageSubscription -> packageSubscriptionMapper.toPackageSubscriptionResponse(packageSubscription)).collect(Collectors
        .toList());
  }

  public void finishSubscription(UUID uuid){
    SkillSubscription packageSubscription=subscriptionRepository.findByUuid(uuid).orElse(null);
    if(packageSubscription!=null){
      packageSubscription.setSubscriptionStatus(PackageStatus.FINISHED);
      subscriptionRepository.save(packageSubscription);
    }
  }

  public List<SkillSubscription> getActiveStudentSkillSubscription() {
    InformationSeeker student=(InformationSeeker)userService.getCurrentUser();
    return subscriptionRepository.findByStudentAndSubscriptionStatus(student, PackageStatus.ACTIVE);
  }

  public boolean isUserSubscribedToACousreSkill(UUID cousreId) {
    TeacherCourse course = teacherCourseRepository.findByUuid(cousreId).orElse(null);
    if(course == null) return false;
    List<SkillSubscription> userSubscriptions = getActiveStudentSkillSubscription();
    if(userSubscriptions.isEmpty()) return false;
    return userSubscriptions.stream().anyMatch(subscription -> subscription.getSkill().getSkillCode().equalsIgnoreCase(course.getSkill().getSkillCode()));
  }
}
