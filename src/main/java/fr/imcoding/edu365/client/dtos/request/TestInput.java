package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestInput {

  @JsonProperty("prenom")
  private String firstName;
  @JsonProperty("nom")
  private String lastName;
}
