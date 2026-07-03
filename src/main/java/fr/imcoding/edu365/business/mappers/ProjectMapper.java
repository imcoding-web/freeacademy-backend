package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.client.dtos.request.ProjectRequest;
import fr.imcoding.edu365.client.dtos.response.ProjectDetails;
import fr.imcoding.edu365.persistence.entities.Project;
import fr.imcoding.edu365.persistence.repositories.ProjectRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 05/06/2022
 */

@Component
@RequiredArgsConstructor
public class ProjectMapper {
private final ProjectRepository projectRepository;
private final SkillMapper skillMapper;
  private final MediaMapper mediaMapper;



  public Project toProject(ProjectRequest projectRequest) {
    return projectRepository.findByUuid(projectRequest.getProjectUuid()).orElse(null);
  }

  public ProjectDetails toProjectDetails(Project project) {
    return new ProjectDetails(project.getUuid(), project.getTitle(), project.getDescription(),
        project.getSkill()!=null?skillMapper.toProjectSkillDto(project.getSkill()):null
           /* .stream().map(skill->skillMapper.toSkillDto(skill)).collect(
            Collectors.toList())*/
        ,project.getMedias().stream().map(media->mediaMapper.toMediaDto(media)).collect(
        Collectors.toList()));
  }

}
