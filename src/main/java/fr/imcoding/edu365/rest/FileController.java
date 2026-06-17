package fr.imcoding.edu365.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.imcoding.edu365.business.services.AnnouncementService;
import fr.imcoding.edu365.business.services.MediaService;
import fr.imcoding.edu365.business.services.ProjectService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.business.services.files.IFileService;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.Announcement;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Project;
import fr.imcoding.edu365.persistence.entities.User;
import lombok.RequiredArgsConstructor;

@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {

  private final IFileService dBFileStorageService;

  private final AnnouncementService announcementService;

  private final MediaService mediaService;

  private final UserService userService;
  private final ProjectService projectService;



  @CrossOrigin
  @PostMapping("/post-media")
  public void uploadLogoFile(@RequestParam("file") MultipartFile[] files,
      @RequestParam(value = "contextUuid", required = false) UUID contextUuid,
      @RequestParam("context") MediaContext context) {
    List<Media> mediaList=new ArrayList<>();

    Arrays.stream(files).forEach(file -> {
      try {
        Media media = mediaService.saveMedia(file, context);
        mediaList.add(media);

         if (context == MediaContext.PICTURE_PROFIL || context == MediaContext.PICTURE_IDENTITY || context == MediaContext.PICTURE_GRADUATION || context == MediaContext.PICTURE_CERTIFICATE || context == MediaContext.CV_DOCUMENT || context == MediaContext.PICTURE_COVER || context == MediaContext.VIDEO || context == MediaContext.VIDEO_PRESENTATION) {
          User user = userService.getCurrentUser();
          if (!user.getMedias().isEmpty()) {
            Optional<Media> picture = user.getMedias().stream()
                .filter(userMedia -> userMedia.getMediaContext() == context)
                .findFirst();
            Media pictureToDelete = picture.isPresent() ? picture.get() : null;
            if (pictureToDelete != null) {
              user.getMedias().remove(pictureToDelete);
            }
          }
          user.getMedias().add(media);
          userService.saveOrUpdateUser(user);
        }


      } catch (Exception e) {
        e.printStackTrace();
      }

    });
     if (context == MediaContext.PICTURE_PROJECT) {
      Project project = projectService.getByUuid(contextUuid);
      if (!project.getMedias().isEmpty()) {
        project.getMedias().addAll(mediaList);
      }else{
         project.setMedias(mediaList);
      }
      projectService.saveOrUpdateProject(project);

    }
    if (context == MediaContext.ANNOUNCEMENT) {
      Announcement announcement = announcementService.getAnnouncementByUUID(contextUuid);
      if (!announcement.getMedias().isEmpty()) {
        announcement.getMedias().addAll(mediaList);
      }else {
        announcement.setMedias(mediaList);
      }
      announcementService.save(announcement);
    }


    // selon le context, on fait le taraitement

  }

  @CrossOrigin
  @GetMapping("/downloadFile/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
      HttpServletRequest request)
      throws Exception {
    Resource resource = dBFileStorageService.loadFileAsResource(fileName);

    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {

    }

    // Fallback to the default content type if type could not be determined
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

  @CrossOrigin
  @GetMapping("delete-user-photo")
  public void deleteUserPhoto(@RequestParam("context") MediaContext context) {
    User user = userService.getCurrentUser();
    if (!user.getMedias().isEmpty()) {
       if (context == MediaContext.PICTURE_COVER || context == MediaContext.PICTURE_PROFIL || context == MediaContext.PICTURE_IDENTITY || context == MediaContext.PICTURE_GRADUATION  || context == MediaContext.PICTURE_CERTIFICATE ||context == MediaContext.CV_DOCUMENT || context == MediaContext.VIDEO || context == MediaContext.VIDEO_PRESENTATION ) {
        Optional<Media> picture = user.getMedias().stream()
            .filter(userMedia -> userMedia.getMediaContext() == context)
            .findFirst();
        Media pictureToDelete = picture.isPresent() ? picture.get() : null;
        if (pictureToDelete != null) {
          user.getMedias().remove(pictureToDelete);
        }
      }

    }
    userService.saveOrUpdateUser(user);
  }

  @CrossOrigin
  @RequestMapping(path = "/download-file", method = RequestMethod.GET)
  public ResponseEntity<Resource> download(@RequestParam("filename") String filepath) throws Exception {
    //Path path =dBFileStorageService.getAbsolutePath(filepath);
    //ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
    Resource resource = dBFileStorageService.loadFileAsResource(filepath);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }


  }
