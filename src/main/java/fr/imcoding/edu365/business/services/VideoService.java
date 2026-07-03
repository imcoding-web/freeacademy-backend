package fr.imcoding.edu365.business.services;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.imcoding.edu365.business.services.files.FilesStorageService;
import fr.imcoding.edu365.persistence.entities.Media;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Rokaya
 * @Date 01/07/2022
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {

  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_LENGTH = "Content-Length";
  public static final String VIDEO_CONTENT = "video/";
  public static final String CONTENT_RANGE = "Content-Range";
  public static final String ACCEPT_RANGES = "Accept-Ranges";
  public static final String BYTES = "bytes";
  public static final int CHUNK_SIZE = 1024 * 1024; // Augmenté à 1MB pour de meilleures performances

  private final FilesStorageService dbFileStorageService;
  private final MediaService mediaService;

  // Cache pour éviter de recharger les mêmes fichiers
  private final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();
  private final LoadingCache<String, byte[]> fileContentCache;
  @Autowired
  public VideoService(FilesStorageService dbFileStorageService, MediaService mediaService) {
    this.dbFileStorageService = dbFileStorageService;
    this.mediaService = mediaService;

    // Initialiser le cache avec une taille maximale et une durée d'expiration
    this.fileContentCache = CacheBuilder.newBuilder()
            .maximumSize(20) // Limite le nombre d'entrées dans le cache
            .expireAfterAccess(10, TimeUnit.MINUTES) // Expire après 10 minutes d'inactivité
            .build(new CacheLoader<String, byte[]>() {
              @Override
              public byte[] load(String key) {
                return dbFileStorageService.load(key);
              }
            });
  }

  /**
   * Prepare the content with StreamingResponseBody pour un streaming efficace.
   *
   * @param mediaUuid UUID.
   * @param range    String.
   * @return ResponseEntity.
   */
  public ResponseEntity<StreamingResponseBody> prepareContent(UUID mediaUuid, final String range) {
    try {
      Media media = mediaService.findByUuid(mediaUuid);
      final String fileKey = media.getMediaLabel();
      final Long fileSize = getFileSize(fileKey);

      long rangeStart = 0;
      long rangeEnd = fileSize - 1;

      // Analyser l'en-tête Range s'il est présent
      if (range != null && range.startsWith("bytes=")) {
        String[] ranges = range.split("-");
        rangeStart = Long.parseLong(ranges[0].substring(6));

        if (ranges.length > 1 && !ranges[1].isEmpty()) {
          rangeEnd = Long.parseLong(ranges[1]);
        } else {
          // Si seul le début est spécifié, limiter à un chunk raisonnable
          rangeEnd = Math.min(rangeStart + CHUNK_SIZE - 1, fileSize - 1);
        }
      }

      // S'assurer que les valeurs sont dans les limites
      rangeStart = Math.max(0, rangeStart);
      rangeEnd = Math.min(rangeEnd, fileSize - 1);
      final long length = rangeEnd - rangeStart + 1;

      // Capturer les valeurs pour utilisation dans la lambda
      final long finalRangeStart = rangeStart;
      final long finalRangeEnd = rangeEnd;

      // Créer le StreamingResponseBody
      StreamingResponseBody responseBody = outputStream -> {
        try {
          readByteRange(fileKey, finalRangeStart, finalRangeEnd, outputStream);
        } catch (Exception e) {
          log.error("Error while streaming video: {}", e.getMessage(), e);
        }
      };

      String contentType = VIDEO_CONTENT + fileKey.substring(fileKey.lastIndexOf(".") + 1);

      return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
              .header(CONTENT_TYPE, contentType)
              .header(ACCEPT_RANGES, BYTES)
              .header(CONTENT_LENGTH, String.valueOf(length))
              .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
              .body(responseBody);

    } catch (Exception e) {
      log.error("Exception while preparing content: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Lire et écrire directement dans le flux de sortie sans charger tout en mémoire.
   *
   * @param filename String.
   * @param start long.
   * @param end long.
   * @param outputStream OutputStream.
   * @throws IOException exception.
   */
  private void readByteRange(String filename, long start, long end, OutputStream outputStream)
          throws IOException, ExecutionException {

    // Utiliser le buffer pour optimiser les performances
    byte[] buffer = new byte[8192]; // Buffer de 8KB

    try (InputStream inputStream = new ByteArrayInputStream(fileContentCache.get(filename))) {
      // Ignorer les bytes avant le début
      long skipped = inputStream.skip(start);
      if (skipped < start) {
        throw new IOException("Impossible de sauter jusqu'au point de départ du streaming");
      }

      // Nombre total d'octets à lire
      long bytesToRead = end - start + 1;
      int read;

      while (bytesToRead > 0 && (read = inputStream.read(buffer, 0, (int) Math.min(buffer.length, bytesToRead))) != -1) {
        outputStream.write(buffer, 0, read);
        bytesToRead -= read;
      }
    }
  }

  /**
   * Obtenir la taille du fichier avec mise en cache.
   *
   * @param fileName String.
   * @return Long.
   */
  public Long getFileSize(String fileName) {
    return fileSizeCache.computeIfAbsent(fileName, key -> {
      byte[] data = dbFileStorageService.load(key);
      return data != null ? (long) data.length : 0L;
    });
  }
}
