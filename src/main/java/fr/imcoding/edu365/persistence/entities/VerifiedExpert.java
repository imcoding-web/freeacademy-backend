package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.dtos.DegreeDto;
import fr.imcoding.edu365.enumeration.ValidationStatus;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * @author Rokaya
 * @Date 16/07/2022
 */
@Entity
@Table(name = "edu365_verified_expert")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifiedExpert extends BaseEntity {
  private String userFirstName;
  private String userLastName;
  private String identityType;
  private String identityNumber;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID identityPictureUuid;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID userPositionUuid;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID userSpeciality;
  private String lastGraduationYear;
  private String currentGraduation;
  private String lastGraduation;
  private String customSpeciality;
  private String customPosition;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID cvDocumentUuid;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID certificateDocumentUuid;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID graduationDocumentUuid;
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID pictureProfileUuid;
  @Enumerated(EnumType.STRING)
  private ValidationStatus status;
  /*@Type(type = "org.hibernate.type.UUIDCharType")
  private UUID userUuid;*/
  @OneToOne
  private InformationGiver user;

  private String refusalReason;

}
