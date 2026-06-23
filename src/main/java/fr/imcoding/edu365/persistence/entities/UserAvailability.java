package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import fr.imcoding.edu365.enumeration.DaysOfWeek;
import fr.imcoding.edu365.utils.Constants;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 03/07/2022
 */
@Entity
@Table(name = "edu365_user_availability")
@Data
@NoArgsConstructor
public class UserAvailability extends BaseEntity{

  private static final long serialVersionUID = 1L;

  private String userAvailabilityTitle;

  @Temporal(TemporalType.TIME)
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      timezone = Constants.DEFAULT_TIMEZONE,
      lenient = OptBoolean.FALSE)
  private Date userAvailabilityStartDate;

  @Temporal(TemporalType.TIME)
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      timezone = Constants.DEFAULT_TIMEZONE,
      lenient = OptBoolean.FALSE)
  private Date userAvailabilityEndDate;

  @Enumerated(EnumType.STRING)
  private DaysOfWeek userAvailabilityDay;

  @ManyToOne
  @JsonIgnore
  private InformationGiver userAvailabilityExpert;

}
