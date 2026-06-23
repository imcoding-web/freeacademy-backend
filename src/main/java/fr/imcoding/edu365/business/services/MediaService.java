package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.services.files.DBFileStorageService;
import fr.imcoding.edu365.business.services.files.FilesStorageService;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.repositories.MediaRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MediaService {

  private final MediaRepository mediaRepository;

  private final FilesStorageService dBFileStorageService;

  public List<Media> getAllMedias() {
    return this.mediaRepository.findAll();
  }

 public Media saveMedia(Media media) {
    return this.mediaRepository.save(media);
  }
  public Media findByUuid(UUID mediaUuid) {
    return this.mediaRepository.findByUuid(mediaUuid);
  }


  public void deleteMedia(Long id) {
    this.mediaRepository.deleteById(id);
  }

  public Media saveMedia(MultipartFile file, MediaContext context) throws Exception {

    String mediaName = dBFileStorageService.storeFile(file);
    String fileDownloadUri = "/file/downloadFile/" + mediaName;

    Media media = new Media();
    media.setMediaContext(context);
    media.setMediaUrl(fileDownloadUri);
    media.setMediaLabel(mediaName);
    media.setMediaSize(file.getSize());
    media.setMediaContentType(file.getContentType());
    media.setOriginalName(file.getOriginalFilename());
    media = mediaRepository.save(media);
    return media;
  }

  public Media findByMediaLabel(String mediaLabel) {
      List<Media> medias = this.mediaRepository.findByMediaLabel(mediaLabel);
      if(medias != null && !medias.isEmpty()) {
          return medias.get(0);
      }
    return null;
  }
}
