package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.utils.Constants;
import fr.imcoding.edu365.utils.Utils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_offer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer extends BaseEntity {

  private static final long serialVersionUID = -4853308925982410868L;

  private Double offerPrice;
  private String offerDescription;
  private String offerSummary;
  private String offerTitle;
  private String offerAdditionalInformations;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date offerDate;
  @Enumerated(EnumType.STRING)
  private OfferStatus offerStatus;
  @ManyToOne(cascade = CascadeType.MERGE)
  private InformationGiver offerGiver;
  @JsonIgnore
  @OneToMany(cascade = CascadeType.MERGE)
  private List<Media> medias = new ArrayList<>();

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date deadlineDeliveringCorrection;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date expertStartAvailabilityDate;
  private Integer videoconferenceDuration;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date videoconferenceStartDate;

  @Temporal(TemporalType.TIME)
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      lenient = OptBoolean.FALSE)
  private Date videoconferenceStartTime;
  private String  cancelingReason;
  private String  wordForExpert;
  private String  wordForAdvertiser;
  private String  uniqueIdentifier;
  private Double  offerVatPrice;
  private Integer  offerPriceToPay;






  @OneToOne
  private Announcement announcement;


  @PostPersist
  public void prePersist() {
    this.uniqueIdentifier = Utils.formatNumber(super.getId());
    if (Math.round(this.offerVatPrice) <= 5) {
      this.offerPriceToPay = 5;
    } else {
      if (Math.round(this.offerVatPrice) % 10 <= 5 && Math.round(this.offerVatPrice) % 10 >= 3) {
        this.offerPriceToPay = (int) Math.round(this.offerVatPrice / 5) * 5;
      } else
        this.offerPriceToPay = (int) Math.round(this.offerVatPrice / 10) * 10;
    }
  }
}