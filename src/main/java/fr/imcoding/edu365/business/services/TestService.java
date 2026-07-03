package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.client.dtos.request.TestInput;
import fr.imcoding.edu365.client.dtos.response.TestOutput;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  public TestOutput getMessage(TestInput input) {

    return TestOutput.from(input);
  }

}
