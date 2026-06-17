package fr.imcoding.edu365.persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonFormat;

import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.ProviderType;
import fr.imcoding.edu365.utils.Utils;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_user")
@Data
@NoArgsConstructor
public class User extends BaseEntity {

  private static final long serialVersionUID = -5021940166648386759L;
  private String uniqueIdentifier;
  private String userEmail;
  private String userPhoneNumber;
  private String userPassword;
  private String userLogin;
  private String userFirstName;
  private String userLastName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date userInscriptionDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date userBirthDate;

//  @JsonIgnore
//  private boolean userIsActif;
  
  @Enumerated(EnumType.STRING)
  protected AccountStatus accountStatus;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Role userRole;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Address userAddress;
  @Enumerated(EnumType.STRING)
  private ProviderType provider;

  @OneToMany(fetch = FetchType.EAGER)
  private List<Media> medias = new ArrayList<>();

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date lastActivationDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date lastDeactivationDate;
  private boolean isFirstConnexion;

  @PrePersist
  public void prePersist() {
    this.userLogin = this.userEmail;
    this.provider = ProviderType.LOCAL;
    //this.accountStatus = AccountStatus.PENDING;
    this.isFirstConnexion=true;
    this.uniqueIdentifier="FA-"+Utils.getSaltString();
  }
  
  public boolean isUserIsActif() {
	  return this.accountStatus == AccountStatus.ACTIVE;
  }
}
