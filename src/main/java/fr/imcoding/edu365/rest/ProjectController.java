package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.ProjectService;
import fr.imcoding.edu365.client.dtos.request.ProjectRequest;
import fr.imcoding.edu365.client.dtos.response.ProjectDetails;
import fr.imcoding.edu365.persistence.entities.Project;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 05/06/2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/project")
public class ProjectController {

  private final ProjectService projectService;

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping()
  public List<ProjectDetails> getUserProject() {
    return this.projectService.getAllByUser();
  }
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Project createUserProject(@ModelAttribute ProjectRequest project) {
    return this.projectService.createUserProject(project);
  }

  @PatchMapping(value="/update-project",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Project updateProject(@ModelAttribute ProjectRequest projectRequest) {
    return this.projectService.updateProject(projectRequest);
  }

  @DeleteMapping("/{projectUuid}")
  public void deleteProject(
      @PathVariable UUID projectUuid) {
    projectService.deleteProject(projectUuid);
  }

}
