package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.enumeration.PrestationStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */
@Entity
@Table(name = "edu365_prestation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestation extends BaseEntity{
  @Enumerated(EnumType.STRING)
  private PrestationStatus prestationStatus;
  @OneToOne
  private Offer offer;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date markAsSolvedDate;
  @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.EAGER)
  private List<Media> medias=new ArrayList<>();
  private String refusalReason;



}
