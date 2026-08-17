package fr.imcoding.edu365.business.services.files;

import fr.imcoding.edu365.business.services.MediaService;
import fr.imcoding.edu365.persistence.entities.Media;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class DBFileStorageService implements FilesStorageService {

  private Path fileStorageLocation;
  private MediaService mediaService;

  @Value("${file.upload-dir}")
  private String uploadDir;

  @PostConstruct
  private void postConstruct() throws Exception {
    this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (IOException ex) {
      throw new Exception("Could not create the directory where the uploaded files will be stored.",
          ex);
    }
  }

  @Override
  public void save(MultipartFile file, String fileStorageName, String path) {
    try {
      Path targetLocation = this.fileStorageLocation.resolve(fileStorageName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file " + fileStorageName + ". Please try again!", ex);
    }
  }

  @Override
  public byte[] load(String filePath) {
    try {
      Path file = this.fileStorageLocation.resolve(filePath).normalize();
      return Files.readAllBytes(file);
    } catch (IOException ex) {
      throw new RuntimeException("Could not read file " + filePath + ". Please try again!", ex);
    }
  }

  @Override
  public void deleteFile(String filePath) {
    try {
      Path file = this.fileStorageLocation.resolve(filePath).normalize();
      Files.deleteIfExists(file);
    } catch (IOException ex) {
      throw new RuntimeException("Could not delete file " + filePath + ". Please try again!", ex);
    }
  }

  @Override
  public String storeFile(MultipartFile file) {
    String[] splitName = file.getOriginalFilename().split("\\.");
    String extension;
    if (splitName.length != 0) {
      extension = splitName[splitName.length - 1];
    } else {
      String[] splitExtension = file.getContentType().split("/");
      extension = splitExtension[1];
    }
    String fileName = RandomStringUtils.random(10, true, true) + "." + extension;

    try {
      if (fileName.contains("..")) {
        throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
      }

      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return fileName;
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
    }
  }

  public Resource loadFileAsResource(String fileName) throws Exception {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new Exception("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new Exception("File not found " + fileName, ex);
    }
  }

  public String storePdfFile(String resourceName, byte[] pdfFile) throws Exception {
    String fileName = resourceName + ".pdf";
    try {
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(new ByteArrayInputStream(pdfFile), targetLocation,
          StandardCopyOption.REPLACE_EXISTING);

      return fileName;
    } catch (Exception ex) {
      throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
    }
  }

  @Async
  public void deleteFile(Media media) {
    try {
      Path filePath = this.fileStorageLocation.resolve(media.getMediaLabel()).normalize();
      Files.delete(filePath);
    } catch (NoSuchFileException x) {
      log.error("%s: no such" + " file", media.getMediaLabel());
    } catch (IOException x) {
      log.error(x.getMessage());
    }
  }

  public Path getAbsolutePath(String fileName) {
    Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
    return filePath;
  }
}
