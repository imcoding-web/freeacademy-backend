package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	private List<Media> medias = new ArrayList<>();

	@ManyToOne
	private SkillArea skillArea;

	@ManyToOne
	private Skill skill;

	@Column(columnDefinition="bit default 0")
	private Boolean isPremium;

}
