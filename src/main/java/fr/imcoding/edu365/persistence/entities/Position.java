package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "edu365_position")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 9083326084374535654L;

  private String positionLabel;
  private String positionCode;
  /*@Fetch(value = FetchMode.SUBSELECT)
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "edu365_position_specialities")
  @JsonIgnore
  private List<Speciality> specialities = new ArrayList<>();*/

}
