package fr.imcoding.edu365.persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.YesNo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_information_giver")
@Data
@NoArgsConstructor
@DiscriminatorValue("information_giver")
public class InformationGiver extends User {

  private static final long serialVersionUID = -3092479031034265593L;

  private double informationGiverRate;

  @Fetch(value = FetchMode.SUBSELECT)
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(name = "edu365_user_skills")
  private List<Skill> skills = new ArrayList<>();

  @ManyToOne(cascade = CascadeType.MERGE)
  private Position userPosition;
  @ManyToOne(cascade = CascadeType.MERGE)
  private Speciality userSpeciality;

  private String identityType;
  private String identityNumber;

  @Column(columnDefinition = "TEXT")
  @Type(type = "text")
  private String userDescription;
  @Column(columnDefinition="bit default 0")
  private boolean isValidate;
  @Column(columnDefinition="bit default 0")
  private boolean exemptFromFees;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date dateValidation;

  //@JsonProperty
  private boolean isTermsAccepted;

  @Column(columnDefinition="bit default 0")
  private boolean homeServices;
  @Column(columnDefinition = "TEXT")
  @Type(type = "text")
  private String homeServicesDescription;

  // champs ajoutés suite à la nouvelle procedure d'inscription
  @Enumerated(EnumType.STRING)
	private YesNo didYouTeach;//
	private String currentSchool;//
	@ManyToOne
	private SkillArea currentLevel;//
	@ManyToOne
	private Skill currentCourse;//
	
	@Enumerated(EnumType.STRING)
	private YesNo previousOnlineTeaching;//
	private String previousOnlinePlatforms;//
	
	@Enumerated(EnumType.STRING)
	private YesNo previousSupportCourses;
	@ManyToOne
	private SkillArea supportCourseLevel;
	@ManyToOne
	private Skill supportCourseCourse;

  @Column(columnDefinition="bit default 0")
  private boolean isElligibleToDoCourses;


  /*@PrePersist
  public void preInformationGiverPersist() {
    this.accountStatus = AccountStatus.PENDING;
  }*/


}
