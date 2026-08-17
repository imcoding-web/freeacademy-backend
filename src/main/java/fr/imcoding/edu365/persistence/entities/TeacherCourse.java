package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.enumeration.CoursePublicationStatus;
import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import fr.imcoding.edu365.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Entity
@Table(name = "edu365_teacher_course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourse extends BaseEntity {

	@ManyToOne
	private InformationGiver creator;
	private String title;
	@Lob
	private String description;
	@Enumerated(EnumType.STRING)
	private CourseType type;

	@Enumerated(EnumType.STRING)
	private Quarter quarter;

	@Fetch(value = FetchMode.SUBSELECT)
	@ManyToMany(cascade = { CascadeType.MERGE}, fetch = FetchType.EAGER)
	private List<Media> medias = new ArrayList<>();

	@ManyToOne
	private SkillArea skillArea;

	@ManyToOne
	private SkillAreaSection skillAreaSection;

	//@ManyToMany
	//private List<SkillAreaSection> skillAreaSections = new ArrayList<>();

	@ManyToOne
	private Skill skill;

	private Boolean isPremium;

	private Boolean shouldBeDisplayed;

	@Enumerated(EnumType.STRING)
	@Column(name = "publication_status")
	private CoursePublicationStatus publicationStatus;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "planned_publication_date_time")
	private LocalDateTime plannedPublicationDateTime;

}
