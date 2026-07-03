package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.PackageType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */

@Entity
@Table(name = "edu365_skill_area_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillAreaPackage extends BaseEntity {

@Enumerated(EnumType.STRING)
private PackageType packageType;
private Double packagePrice;
@ManyToOne
private SkillArea skillLevel;



}
