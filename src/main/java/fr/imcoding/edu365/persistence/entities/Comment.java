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
@Table(name = "edu365_comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

  private static final long serialVersionUID = -4853308925982410868L;

  private String commentText;
  @ManyToOne(cascade = CascadeType.MERGE)
  private User commentUser;

  @OneToMany(cascade = CascadeType.MERGE)
  private List<Media> medias = new ArrayList<>();

}
