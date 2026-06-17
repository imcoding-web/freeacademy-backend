package fr.imcoding.edu365.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

import fr.imcoding.edu365.enumeration.RegisterStatus;
import fr.imcoding.edu365.enumeration.YesNo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_teacher_register_request")
@Data
@NoArgsConstructor
public class TeacherRegisterRequest extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3092479031034265593L;
	private String uniqueIdentifier;
	private String userEmail;//
	private String userPhoneNumber;//
	//private String userPassword;//
	private String userFirstName;//
	private String userLastName;//
	private String userLogin;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.DATE)
	private Date userRegisterDate;//

	@Enumerated(EnumType.STRING)
	protected RegisterStatus registerStatus;//

	@OneToMany(fetch = FetchType.EAGER)
	private List<Media> medias = new ArrayList<>();

	private String identityType;//
	private String identityNumber;//
	
	 @ManyToOne(cascade = CascadeType.MERGE)
	  private Role userRole;//

	@Column(columnDefinition = "TEXT")
	@Type(type = "text")
	private String description;//

	private boolean isTermsAccepted;//
	
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
	
	//if the register request is from an existing teacher
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID existingTeacherUuid;

	@PrePersist
	public void preInformationGiverPersist() {
		this.registerStatus = RegisterStatus.PENDING;
		this.userLogin = this.userEmail;
	    //this.uniqueIdentifier="FA-"+Utils.getSaltString();

	}
	
	

}
