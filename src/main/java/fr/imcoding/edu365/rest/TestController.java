package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.TestService;
import fr.imcoding.edu365.client.dtos.request.TestInput;
import fr.imcoding.edu365.client.dtos.response.TestOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {


  private final TestService testService;

  @GetMapping
  public TestOutput getMessage(@RequestBody TestInput input) {
    return testService.getMessage(input);
  }

}
