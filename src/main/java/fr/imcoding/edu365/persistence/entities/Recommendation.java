package fr.imcoding.edu365.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation extends BaseEntity {

  private static final long serialVersionUID = 8413890811331134424L;

  private String recommendationText;
  @ManyToOne(cascade = CascadeType.MERGE)
  private User recommendationWriterUser;
  @ManyToOne(cascade = CascadeType.MERGE)
  private User recommendationConcernedUser;

  @OneToMany(cascade = CascadeType.MERGE)
  private List<Media> medias = new ArrayList<>();

}
