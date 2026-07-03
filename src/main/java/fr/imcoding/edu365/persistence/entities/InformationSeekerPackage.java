package fr.imcoding.edu365.persistence.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_information_seeker_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationSeekerPackage extends BaseEntity {

	private static final long serialVersionUID = -4853308925982410868L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date packageEndDate;

	@Enumerated(EnumType.STRING)
	private PackageStatus packageStatus;
	@ManyToOne(cascade = CascadeType.MERGE)
	private InformationSeeker packageRelatedStudent;
	@ManyToOne(cascade = CascadeType.MERGE)
	private StudyPackage selectedPackage;
	
	private int consumedHours;
	
	public boolean isActive() {
		// la condition  consumedHours < Constants.STUDY_PACKAGE_MAX_HOURS peut etre utilisé aprés une fois on fait la mise à jour de consumedHours 
		return packageStatus == PackageStatus.ACTIVE && consumedHours < Constants.STUDY_PACKAGE_MAX_HOURS;
	}
	
	public boolean isValid(SkillArea skillArea) {
		return selectedPackage.getSkillArea().getSkillAreaCode().equals(skillArea.getSkillAreaCode());
	}

}