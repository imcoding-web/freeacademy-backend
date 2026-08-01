package fr.imcoding.edu365.persistence.specifications;

import fr.imcoding.edu365.dtos.TeacherCourseSearchCriteria;
import fr.imcoding.edu365.enumeration.CoursePublicationStatus;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.SkillAreaSection;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Rokaya
 * @Date 19/09/2023
 */
public class TeacherCourseSpecification {
  public static Specification<TeacherCourse> createAnnouncementSpecifications(TeacherCourseSearchCriteria searchCriteria) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (searchCriteria.getSkillArea() != null && !searchCriteria.getSkillArea().isEmpty()) {
        Join<TeacherCourse, SkillArea> skillSkillAreaJoin = root.join("skillArea", JoinType.INNER);
        predicates.add(skillSkillAreaJoin.get("skillAreaCode").in(searchCriteria.getSkillArea()));
      }
      if (searchCriteria.getSkillAreaSection() != null && !searchCriteria.getSkillAreaSection().isEmpty()) {
        Join<TeacherCourse, SkillAreaSection> skillSkillAreaSectionJoin = root.join("skillAreaSection", JoinType.INNER);
        predicates.add(skillSkillAreaSectionJoin.get("code").in(searchCriteria.getSkillAreaSection()));
      }
      if (searchCriteria.getSkill() != null && !searchCriteria.getSkill().isEmpty()) {
        Join<TeacherCourse, Skill> courseSkillJoin = root.join("skill", JoinType.INNER);
        predicates.add(courseSkillJoin.get("skillLabel").in(searchCriteria.getSkill()));
      }

      if (searchCriteria.getType() != null) {
        predicates.add(builder.equal(root.get("type"), searchCriteria.getType()));
      }

      if (searchCriteria.getQuarter() != null) {
        predicates.add(builder.equal(root.get("quarter"), searchCriteria.getQuarter()));
      }
      if(Boolean.TRUE.equals(searchCriteria.getShouldBeDisplayed())) {
        predicates.add(builder.isTrue(root.get("shouldBeDisplayed")));
      }
      if (searchCriteria.getPublicationStatus() != null) {
        predicates.add(builder.equal(root.get("publicationStatus"), searchCriteria.getPublicationStatus()));
      }
      return builder.and(predicates.toArray(new Predicate[0]));

    };
  }

}
