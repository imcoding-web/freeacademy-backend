package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.MediaMapper;
import fr.imcoding.edu365.business.mappers.ProjectMapper;
import fr.imcoding.edu365.business.mappers.SkillMapper;
import fr.imcoding.edu365.business.services.files.DBFileStorageService;
import fr.imcoding.edu365.business.services.files.FilesStorageService;
import fr.imcoding.edu365.client.dtos.request.ProjectRequest;
import fr.imcoding.edu365.client.dtos.response.ProjectDetails;
import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.exceptions.UserForbiddenException;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Project;
import fr.imcoding.edu365.persistence.repositories.ProjectRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Rokaya
 * @Date 04/06/2022
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

  private final UserService userService;
  private final SkillMapper skillMapper;
  private final ProjectMapper projectMapper;
  private final MediaMapper mediaMapper;
  private final ProjectRepository projectRepository;
  private final FilesStorageService dBFileStorageService;
  private final MediaService mediaService;


  public Project getProjectByUUID(UUID projectUuid) {
    return this.projectRepository.findByUuid(projectUuid).orElse(null);
  }

  public Project createUserProject(ProjectRequest projectRequest) {
    Project project = new Project();
    InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
    project.setProjectOwner(currentUser);
    project.setDescription(projectRequest.getDescription());
    project.setTitle(projectRequest.getTitle());
    project.setSkill(skillMapper.toSkill(projectRequest.getSkill()));
    List<Media> mediaList = new ArrayList<>();
    if (projectRequest.getFiles()!=null && projectRequest.getFiles().size() > 0) {
      projectRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.PICTURE_PROJECT);
          mediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      project.getMedias().addAll(mediaList);
    }

    return projectRepository.save(project);
  }

  public Project saveOrUpdateProject(Project project) {
    return this.projectRepository.save(project);
  }

  public List<ProjectDetails> getAllByUser() {
    InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
    return projectRepository.getByProjectOwnerUuid(currentUser.getUuid()).stream()
        .map(project -> projectMapper.toProjectDetails(project)).collect(Collectors.toList());
  }

  public List<ProjectDetails> getAllByUserUuid(UUID uuid) {
    InformationGiver currentUser = (InformationGiver) userService.getUserByUUID(uuid);
    return projectRepository.getByProjectOwnerUuid(currentUser.getUuid()).stream()
        .map(project -> projectMapper.toProjectDetails(project)).collect(Collectors.toList());
  }
  public Project getByUuid(UUID uuid) {
    return projectRepository.findByUuid(uuid).orElse(null);
  }


  @Transactional
  public Project updateProject(ProjectRequest projectRequest) {
    List<Media> mediaListToDelete = checkMediaProject(projectRequest.getProjectUuid(),
        projectRequest.getMedias());
    List<Media> newMediaList =new ArrayList<>();

    Project projectToUpdate = projectMapper.toProject(projectRequest);
    if (!projectToUpdate.getProjectOwner().getUuid()
        .equals(userService.getCurrentUser().getUuid())) {
      throw new UserForbiddenException("Cannot update project for other user");
    }

    projectToUpdate.setTitle(projectRequest.getTitle());
    projectToUpdate.setDescription(projectRequest.getDescription());
    projectToUpdate.setSkill(skillMapper.toSkill(projectRequest.getSkill()));
   if (!mediaListToDelete.isEmpty()) {
      mediaListToDelete.forEach(media -> {
        mediaService.deleteMedia(media.getId());
        dBFileStorageService.deleteFile(media.getMediaLabel());
      });

      projectToUpdate.getMedias().removeAll(mediaListToDelete);

    }
    if (projectRequest.getFiles()!=null && projectRequest.getFiles().size() > 0) {
      projectRequest.getFiles().forEach(item -> {
        try {
          Media media = mediaService.saveMedia(item, MediaContext.PICTURE_PROJECT);
          newMediaList.add(media);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      projectToUpdate.getMedias().addAll(newMediaList);
    }
    Project project = projectRepository.save(projectToUpdate);


    return project;
  }

  @Transactional
  public void deleteProject(UUID projectUuid) {
    log.info("Delete project with ID {}", projectUuid);
    Project projectTodelete = getByUuid(projectUuid);
    if (!projectTodelete.getProjectOwner().getUuid()
        .equals(userService.getCurrentUser().getUuid())) {
      throw new UserForbiddenException("Cannot delete project for other user");
    }
    if (projectTodelete != null) {
      List<Media> projectMedia = projectTodelete.getMedias();
      projectRepository.delete(projectTodelete);
      // remove media from disk
      projectMedia.stream().forEach(media -> {
        dBFileStorageService.deleteFile(media.getMediaLabel());
      });
    }
  }

  public List<Media> checkMediaProject(UUID projectUuid, List<MediaDto> newMediaList) {

    Project projectToUpdate = getByUuid(projectUuid);
    List<Media> mediaList = new ArrayList<>();
  if(newMediaList!=null &&!newMediaList.isEmpty()) {
  if (projectToUpdate != null) {
     ProjectDetails p = projectMapper.toProjectDetails(projectToUpdate);
    if (!projectToUpdate.getMedias().isEmpty()) {
      mediaList = p.getMedias().stream().filter(media -> !newMediaList.contains(media))
          .collect(Collectors.toList()).stream().map(media -> mediaMapper.toMedia(media))
          .collect(Collectors.toList());
    }
  }

}
    return mediaList;
  }
}
