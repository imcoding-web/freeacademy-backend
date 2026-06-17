package fr.imcoding.edu365.business.services.files;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class LocalFileService implements FilesStorageService {
	private static final Logger log = LoggerFactory.getLogger(LocalFileService.class);

	@Value("${file.upload-dir}")
	private String UPLOAD_DIR;
	private static final Logger LOG = LoggerFactory.getLogger(LocalFileService.class);
	@Override
	public void save(MultipartFile file, String fileStorageName, String path) {
		try {
			Path uploadDest =  this.getFileUploadRoot(path).resolve(fileStorageName);
			if (uploadDest.toFile().exists()) {
				File destFile = uploadDest.resolve(String.valueOf(System.currentTimeMillis())).toFile();
				destFile.getParentFile().mkdirs();
				uploadDest.toFile().renameTo(destFile);
			}
			Files.copy(file.getInputStream(), uploadDest);
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public byte[] load(String filePath) {
		try {

			Path file = this.getFileUploadRoot(filePath);

			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				try (InputStream inputStream = resource.getInputStream();
					 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					return outputStream.toByteArray();
				} catch (IOException e) {
					throw new RuntimeException("Error reading file content: " + e.getMessage());
				}
			} else {
				throw new RuntimeException("COULD NOT READ THE FILE!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	@Override
	public void deleteFile(String filePath) {
		try {
			Path file = this.getFileUploadRoot(filePath);
			file.toFile().delete();
		} catch (Exception e) {
			LOG.error("Error: " + e.getMessage());
		}
	}

	@Override
	public String storeFile(MultipartFile file) {
		String[] splitName = file.getOriginalFilename().split("\\.");
		String extension = null;
		if (splitName.length != 0) {
			extension = splitName[splitName.length - 1];
		} else {
			String[] splitExtension = file.getContentType().split("/");
			extension = splitExtension[1];
		}
		String fileName = RandomStringUtils.random(10, true, true) + "." + extension;

		try {


			Path targetLocation = Paths.get(fileName);
			Path fileStorageLocation = Paths.get(UPLOAD_DIR)
					.toAbsolutePath().normalize();
			Path path = fileStorageLocation.resolve(targetLocation);
			Files.createDirectories(path.getParent());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			return targetLocation.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Path getFileUploadRoot(String path) {
		Path filePath = Paths.get(UPLOAD_DIR + File.separator + path);
		if (filePath.toFile().exists() || filePath.toFile().mkdirs()) {
			return filePath;
		}
		return Paths.get(UPLOAD_DIR);
	};

	private Path getFileUploadRoot() {
		return Paths.get(UPLOAD_DIR);
	};
}
