package fr.imcoding.edu365.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.OptBoolean;

import fr.imcoding.edu365.client.dtos.response.PrestationMeetingResponse;
import fr.imcoding.edu365.client.dtos.response.User_Details;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import fr.imcoding.edu365.enumeration.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rokaya
 * @Date 03/09/2022
 */
@Data
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class OfferResponseDto {

	private UUID offerUuid;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE)
	private Date videoconferenceDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE)
	private Date correctionDeliveryDate;
	private CourseCreatorDto offerExpert;
	private User_Details offerClient;
	private OfferStatus offerStatus;
	private String wordForExpert;
	private Double offerPrice;
	private String offerDescription;
	private UUID announcementUuid;
	private InterventionType announcementInterventionType;
	private AnnouncementType announcementType;
	private String announcementTitle;
	private String announcementDescription;
	private SkillAreaDto announcementSkillLevel;
	private SkillDto announcementSkill;
	private List<MediaDetails> announcementMedias;

	private Integer videoconferenceDuration;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE)
	private Date deadlineDeliveringCorrection;
	private String wordForAdvertiser;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date expertStartAvailabilityDate;
	private int hoursNumber;
	private String uniqueIdentifier;
	private Double offerVatprice;
	private Integer offerPriceToPay;

	private PrestationMeetingResponse meetingResponse;

	private boolean inStudyPackage;

}
