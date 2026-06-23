package fr.imcoding.edu365.persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_announcement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = -1442603839970591661L;

  @Lob
  private String announcementDescription;
  private String announcementTitle;
  @Enumerated(EnumType.STRING)
  private AnnouncementStatus announcementStatus;
  private Integer announcementNumberLike = 0;
  private Integer announcementNumberDislike = 0;
  @Enumerated(EnumType.STRING)
  private AnnouncementType announcementType;
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Address announcementAddress;
  @Lob
  private String announcementSummary;
  @Lob
  private String announcementAdditionalInformations;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date announcementEndAvailableDate; // date limite pour récevoir des offres

  @ManyToOne
  private Administrator announcementAdminApprouved;
  @ManyToOne(cascade = CascadeType.MERGE)
  private User announcementPublisher;

  @Fetch(value = FetchMode.SUBSELECT)
  @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
  private List<Media> medias = new ArrayList<>();
  /*@Fetch(value = FetchMode.SUBSELECT)
  @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
  private List<Offer> offers = new ArrayList<>();*/
  @Fetch(value = FetchMode.SUBSELECT)

  @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
  private List<User> likers = new ArrayList<>();
  @Fetch(value = FetchMode.SUBSELECT)

  @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
  private List<User> dislikers = new ArrayList<>();
  @Fetch(value = FetchMode.SUBSELECT)

  @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
  private List<Comment> comments = new ArrayList<>();

  @ManyToOne
  private SkillArea announcementSkillArea;

  @ManyToMany
  @JsonIgnore
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<SkillAreaSection> skillAreaSections;

  @ManyToOne
  private Skill skill;

  @Enumerated(EnumType.STRING)
  private InterventionType interventionType;

  private int hoursNumber;
  private String refusalReason;
  
  private boolean homeService;
  
  @Column(columnDefinition = "TEXT")
  @Type(type = "text")
  private String homeServiceDetails;
  
  private boolean inStudyPackage;
  
  private int estimatedPriceToPay;
  private int estimatedHourNumber;




}
