package fr.imcoding.edu365.persistence.specifications;

import fr.imcoding.edu365.dtos.AnnouncementSearchCriteria;
import fr.imcoding.edu365.dtos.SimilarAnnouncementSearchCriteria;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import fr.imcoding.edu365.persistence.entities.Announcement;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Rokaya
 * @Date 30/07/2022
 */
public class AnnouncementSpecifications {
  public static List<AnnouncementStatus> announcementStatuses=new ArrayList<>(Arrays.asList(AnnouncementStatus.PUBLISHED, AnnouncementStatus.RECEIVED_OFFER));

  public static Specification<Announcement> createAnnouncementSpecifications(AnnouncementSearchCriteria searchCriteria) {

    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (!CollectionUtils.isEmpty(searchCriteria.getSkillLevels())) {
        Join<Announcement, SkillArea> announcementSkillAreaJoin = root.join("announcementSkillArea", JoinType.INNER);
        predicates.add(announcementSkillAreaJoin.get("skillAreaLabel").in(searchCriteria.getSkillLevels()));
      }

      if (!CollectionUtils.isEmpty(searchCriteria.getSkills())) {
        Join<Announcement, Skill> announcementSkillJoin = root.join("skill", JoinType.INNER);
        predicates.add(announcementSkillJoin.get("skillLabel").in(searchCriteria.getSkills()));
      }

      if (!CollectionUtils.isEmpty(searchCriteria.getAnnouncementTypes())) {
        predicates.add(root.get("announcementType").in(searchCriteria.getAnnouncementTypes().stream().map(type->AnnouncementType.valueOf(type)).collect(Collectors
            .toList())));
      }


      if (!CollectionUtils.isEmpty(searchCriteria.getInterventionTypes())) {
        predicates.add(root.get("interventionType").in(searchCriteria.getInterventionTypes().stream().map(type->InterventionType
            .valueOf(type)).collect(Collectors.toList())));
      }


      if(searchCriteria.getEndAvailableDate()!=null && !searchCriteria.getEndAvailableDate().isEmpty()) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
          Date date = formatter.parse(searchCriteria.getEndAvailableDate());
          predicates.add(builder
              .greaterThanOrEqualTo(root.get("announcementEndAvailableDate").as(Date.class), date));
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      predicates.add(root.get("announcementStatus").in(announcementStatuses));
      query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }


  public static Specification<Announcement> createSimilarAnnouncementSpecifications(SimilarAnnouncementSearchCriteria searchCriteria) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Join<Announcement, Skill> announcementSkillJoin = root.join("skill", JoinType.INNER);

      switch (searchCriteria.getFieldName()) {
        case "skillArea":
          //predicates.add(builder.equal(announcementSkillJoin.get("skillLabel"), searchCriteria.getFieldValue()));

          Join<Announcement, SkillArea> announcementSkillAreaJoin = root
              .join("announcementSkillArea", JoinType.INNER);
          predicates.add(builder.equal(
              announcementSkillAreaJoin.get("skillAreaLabel"),(searchCriteria.getFieldValue())));

          break;
        case "skill":
          predicates.add(builder.equal(announcementSkillJoin.get("skillLabel"), searchCriteria.getFieldValue()));

          break;
        case "announcementTypes":
          predicates.add(builder.equal(root.get(searchCriteria.getFieldName()), searchCriteria.getFieldValue()));

          break;
        case "interventionTypes":
          predicates.add(builder.equal(root.get(searchCriteria.getFieldName()), searchCriteria.getFieldValue()));
          break;


      }


         predicates.add(builder.notEqual(root.get("uuid"), searchCriteria.getAnnouncementUuid()));
         predicates.add(root.get("announcementStatus").in(announcementStatuses));
         query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }


}