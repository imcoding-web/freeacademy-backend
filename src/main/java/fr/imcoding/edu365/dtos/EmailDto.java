package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.EmailContext;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

 // private String to;

  private String subject;

  private String templateName;

  private Map<String, Object> maps;

  private Map<String, byte[]> attachments;

  private EmailContext emailContext;


}
