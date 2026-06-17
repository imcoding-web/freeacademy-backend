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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "edu365_trace_teacher_course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraceTeacherCourse extends BaseEntity {
	private String courseTitle;
	private String userFullName;
	private String userEmail;
	private String userPhoneNumber;

	@ManyToOne
	private TeacherCourse teacherCourse;

	@ManyToOne
	private User user;

}
