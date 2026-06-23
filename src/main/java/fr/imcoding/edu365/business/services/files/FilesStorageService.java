package fr.imcoding.edu365.business.services.files;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {

	void save(MultipartFile file, String fileStorageName, String path);

	byte[] load(String filePath);

	void deleteFile(String filePath);

	String storeFile(MultipartFile file);
}
