package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//entité pour gérer les inscriptions mensuelles
@Entity
@Table(name = "edu365_study_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyPackage extends BaseEntity {

	private static final long serialVersionUID = -4853308925982410868L;

	private Double packagePrice;
	private String packageCode;
	private String packageLabel;
	private String packageDetails;
	private int packageMaxHours = 18;
	@ManyToOne(cascade = CascadeType.MERGE)
	private SkillArea skillArea;

	@PrePersist
	public void prepersist() {
		this.packageCode = this.skillArea.getSkillAreaCode();
	}

}