package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.imcoding.edu365.client.dtos.request.TestInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestOutput {

  @JsonProperty("prenom")
  private String firstName;
  @JsonProperty("nom")
  private String lastName;
  @JsonProperty("votre_message")
  private String message;

  public static TestOutput from(TestInput input) {

    return new TestOutput(input.getFirstName(), input.getLastName(),
        "salut " + input.getFirstName() + " " + input.getLastName());
  }
}
