package fr.imcoding.edu365.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@Entity
@Table(name = "edu365_lesson_correction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonCorrection extends BaseEntity {
  @Lob
  private String description ;
  private boolean onlySubscribedUsers;

  @Fetch(value = FetchMode.SUBSELECT)
  @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.EAGER)
  private List<Media> medias = new ArrayList<>();

  @OneToOne
  private TeacherCourse course;
}
