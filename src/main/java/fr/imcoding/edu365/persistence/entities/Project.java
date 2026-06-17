package fr.imcoding.edu365.persistence.entities;

import com.mysql.cj.protocol.ColumnDefinition;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 04/06/2022
 */
@Entity
@Table(name = "edu365_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

  private String title;

  @Column(columnDefinition = "TEXT")
  @Type(type = "text")
  private String description;

  @Fetch(value = FetchMode.SUBSELECT)
  @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.EAGER)
  private List<Media> medias = new ArrayList<>();


/*  @Fetch(value = FetchMode.SUBSELECT)
  @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
  @JoinTable(name="edu365_project_skills")
  private List<Skill> skills=new ArrayList<>();*/
  @ManyToOne(fetch = FetchType.EAGER)
  private Skill skill ;

  @ManyToOne(fetch = FetchType.EAGER)
  private InformationGiver projectOwner ;




}
