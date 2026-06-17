package fr.imcoding.edu365.business.services.files;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class RemoteFileService implements FilesStorageService {
	private static final Logger logger = LoggerFactory.getLogger(RemoteFileService.class);

	@Autowired
	private AmazonS3 s3Client;

	@Value("${storage.spaces.bucket}")
	private String spaceBucket;



	@Override
    public void save(MultipartFile file, String fileStorageName, String path) {
        // Créer les métadonnées de l'objet
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Construire le chemin complet de l'objet dans S3
        String fullPath = path + "/" + fileStorageName;

        // Créer la requête pour mettre l'objet dans S3
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(spaceBucket, fullPath, file.getInputStream(), metadata);
                  //  .withCannedAcl(CannedAccessControlList.PublicRead);
            // Envoyer le fichier à S3
            s3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public byte[] load(String filePath) {

		// Récupérer l'objet de S3
		S3Object s3Object = s3Client.getObject(spaceBucket, filePath);


		try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
			// Convertir le flux en byte[]
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error reading file content: " + e.getMessage());
        }
    }

	@Override
	public void deleteFile(String filePath) {
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(spaceBucket, filePath);
		// Supprimer l'objet
		s3Client.deleteObject(deleteObjectRequest);

	}

	@Override
	public String storeFile(MultipartFile file) {
		// Normalize file name
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

			// Create the metadata for the object
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType());

			// Create the request to put the object in S3
			PutObjectRequest putObjectRequest = new PutObjectRequest(spaceBucket, fileName, file.getInputStream(), metadata);

			// Upload the file to S3
			s3Client.putObject(putObjectRequest);

			return fileName;
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}
}
