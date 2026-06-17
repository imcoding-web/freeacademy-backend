package fr.imcoding.edu365.client.dtos.request;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestationAdminRequest {

	private String subject;
	private AnnouncementType type;
	private InterventionType interventionType;
	private String description;
	private String teacherEmail;
	private String studentEmail;
	private String skillAreaCode;
	private String skillCode;
	private List<MultipartFile> files = new ArrayList<>();
	private Double price;
	private int estimatedHourNumber;

}
