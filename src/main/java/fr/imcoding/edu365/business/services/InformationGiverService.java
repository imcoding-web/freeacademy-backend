package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.UserMapper;
import fr.imcoding.edu365.dtos.UserDetails;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.imcoding.edu365.business.mappers.AddressMapper;
import fr.imcoding.edu365.business.mappers.DegreeMapper;
import fr.imcoding.edu365.business.mappers.ExpertDescriptionMapper;
import fr.imcoding.edu365.business.mappers.ExpertDetailsMapper;
import fr.imcoding.edu365.business.mappers.MediaMapper;
import fr.imcoding.edu365.business.mappers.PositionMapper;
import fr.imcoding.edu365.business.mappers.PositionRequestMapper;
import fr.imcoding.edu365.business.mappers.SpecialityMapper;
import fr.imcoding.edu365.business.mappers.TeacherRegisterRequestMapper;
import fr.imcoding.edu365.business.mappers.UserBankDataMapper;
import fr.imcoding.edu365.business.mappers.VerifiedExpertMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.request.ExpertDescriptionRequest;
import fr.imcoding.edu365.client.dtos.request.ExpertRegisterRequest;
import fr.imcoding.edu365.client.dtos.request.PositionRequest;
import fr.imcoding.edu365.client.dtos.request.UserBankDataRequest;
import fr.imcoding.edu365.client.dtos.request.UserRequest;
import fr.imcoding.edu365.client.dtos.response.ExpertResponse;
import fr.imcoding.edu365.client.dtos.response.PositionResponse;
import fr.imcoding.edu365.client.dtos.response.UserBankDataResponse;
import fr.imcoding.edu365.client.dtos.response.ValidationExpertResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.ExpertDescription;
import fr.imcoding.edu365.dtos.ExpertDetails;
import fr.imcoding.edu365.dtos.ExpertHomeServiceDto;
import fr.imcoding.edu365.dtos.ExpertProfileDto;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.ValidationAccountResponse;
import fr.imcoding.edu365.dtos.ValidationHistoryDto;
import fr.imcoding.edu365.dtos.VerifiedFieldDto;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.IdentityType;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.enumeration.ValidationStatus;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Role;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.TeacherRegisterRequest;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import fr.imcoding.edu365.persistence.entities.UserBankData;
import fr.imcoding.edu365.persistence.entities.UserFees;
import fr.imcoding.edu365.persistence.entities.VerifiedExpert;
import fr.imcoding.edu365.persistence.repositories.InformationGiverRepository;
import fr.imcoding.edu365.persistence.repositories.RoleRepository;
import fr.imcoding.edu365.persistence.repositories.TeacherRegisterRequestRepository;
import fr.imcoding.edu365.persistence.repositories.UserRepository;
import fr.imcoding.edu365.persistence.repositories.VerifiedExpertRepository;
import fr.imcoding.edu365.utils.Constants;
import fr.imcoding.edu365.utils.Utils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InformationGiverService {

	private final InformationGiverRepository informationGiverRepository;
	
	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final UserService userService;

	private final PasswordEncoder encoder;

	private final PositionRequestMapper positionRequestMapper;
	private final UserBankDataMapper userBankDataMapper;
	private final UserBankDataService userBankDataService;

	private final PositionMapper positionMapper;
	private final SpecialityMapper specialityMapper;
	private final UserAdditionalInfoService userAdditionalInfoService;
	private final ExpertDetailsMapper expertDetailsMapper;
	private final ExpertDescriptionMapper expertDescriptionMapper;

	private final AddressMapper addressMapper;
	private final MediaMapper mediaMapper;

	private final VerifiedExpertService verifiedExpertService;
	private final VerifiedExpertMapper verifiedExpertMapper;
	private final TeacherRegisterRequestMapper teacherRegisterRequestMapper;
	private final VerifiedExpertRepository verifiedExpertRepository;
	private final DegreeMapper degreeMapper;
	private final MediaService mediaService;
	private final EmailService emailService;

	private final UserFeesService userFeesService;
	
	private final SkillAreaService skillAreaService;
	
	private final SkillService skillService;
	private final UserMapper userMapper;

	private final TeacherRegisterRequestRepository teacherRegisterRequestRepository;

	public void saveUser(UserRequest userIn) {
		if (!userService.checkUserPhoneNumber(userIn.getUserPhoneNumber())
				&& !userService.checkUserEmail(userIn.getUserEmail())) {
			InformationGiver user = new InformationGiver();
			user.setUserEmail(userIn.getUserEmail());
			user.setUserFirstName(userIn.getUserFirstName());
			user.setUserLastName(userIn.getUserLastName());
			user.setUserPassword(encoder.encode(userIn.getUserPassword()));
			user.setUserPhoneNumber(userIn.getUserPhoneNumber());
			Role lodgerRole = this.roleRepository.findByRoleCode(RoleCode.INFORMATION_GIVER);
			user.setUserRole(lodgerRole);
			user.setUserInscriptionDate(new Date());
			user.setIdentityType(userIn.getUserIdentityType());
			user.setIdentityNumber(userIn.getUserIdentityNumber());
			user.setTermsAccepted(userIn.isTermsAccepted());
			UUID uuid = informationGiverRepository.save(user).getUuid();
			userBankDataService.initExpertBankData(user);
			userService.sendActivationEmail(uuid);
			// send email to al ladministrator
			// notifAdministrators(user,Constants.CREATION);
		}

	}

	public void notifAdministrators(User user, String type) {
		List<String> listAdministrator = userService.getUserDetailsyRole(RoleCode.ADMINISTRATOR).stream()
				.map(u -> u.getUserEmail()).collect(Collectors.toList());
		if (listAdministrator != null && !listAdministrator.isEmpty()) {
			Map<String, Object> maps = new HashMap<>();
			maps.put("prenom", user.getUserFirstName());
			maps.put("nom", user.getUserLastName());
			maps.put("email", user.getUserEmail());
			maps.put("telephone", user.getUserPhoneNumber());
			EmailDto userEmailDto = new EmailDto(
					type.equals(Constants.CREATION) ? Constants.MAIL_SUBJECT_ADMIN_CREATE_PROFESSIONAL_ACCOUNT
							: Constants.MAIL_SUBJECT_ADMIN_VALIDATE_ACCOUNT_REQUEST,
					type.equals(Constants.CREATION) ? "notif-admin-create-professional-account.html"
							: "notif-admin-validate-account-request.html",
					maps, new HashMap<>(), EmailContext.NOTIF_ADMIN_VALIDATE_ACCOUNT_REQUEST);
			emailService.sendMail(userEmailDto, listAdministrator);
		}

	}

	public ExpertHomeServiceDto updateExpertHomeService(ExpertHomeServiceDto homeServiceDto) {
		InformationGiver user = (InformationGiver) userService.getCurrentUser();
		user.setHomeServices(homeServiceDto.isHomeServices());
		user.setHomeServicesDescription(homeServiceDto.getHomeServicesDescription());
		informationGiverRepository.save(user);
		return ExpertHomeServiceDto.builder().homeServices(user.isHomeServices())
				.homeServicesDescription(user.getHomeServicesDescription()).build();

	}

	public ExpertHomeServiceDto getExpertHomeServiceData() {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		return ExpertHomeServiceDto.builder().homeServices(currentUser.isHomeServices())
				.homeServicesDescription(currentUser.getHomeServicesDescription()).build();
	}

	public ExpertDetails patchUser(ExpertDetails userRequest) {
		InformationGiver user = (InformationGiver) userService.getCurrentUser();
		user.setUserFirstName(userRequest.getUserFirstName());
		user.setUserLastName(userRequest.getUserLastName());
		user.setUserEmail(userRequest.getUserEmail());
		user.setUserAddress(
				userRequest.getUserAddress() != null ? addressMapper.toAddress(userRequest.getUserAddress()) : null);
		user.setIdentityType(userRequest.getUserIdentityType());
		user.setIdentityNumber(userRequest.getUserIdentityNumber());
		informationGiverRepository.save(user);
		boolean accountStatus = verifiedExpertService.checkCompletedValidation(user.getUuid());
		return expertDetailsMapper.toExpertDetailsResponse(user, accountStatus);
	}

	public PositionResponse updateUserPosition(PositionRequest positionRequest) {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		UserAdditionalInfo userAdditionalInfo = userAdditionalInfoService.getUserAdditionalInfo(currentUser.getUuid());
		currentUser.setUserPosition(positionMapper.toPosition(positionRequest.getUserPosition()));
		currentUser.setUserSpeciality(positionRequest.getUserSpeciality() != null
				? specialityMapper.toSpeciality(positionRequest.getUserSpeciality())
				: null);
		if (userAdditionalInfo != null) {
			if (positionRequest.getCurrentGraduation() != null) {
				userAdditionalInfo.setCurrentGraduation(degreeMapper.toDegree(positionRequest.getCurrentGraduation()));
			} else {
				userAdditionalInfo.setCurrentGraduation(null);
			}
			if (positionRequest.getLastGraduation() != null) {
				userAdditionalInfo.setLastGraduation(degreeMapper.toDegree(positionRequest.getLastGraduation()));

			} else
				userAdditionalInfo.setLastGraduation(null);

			userAdditionalInfo.setLastGraduationYear(positionRequest.getLastGraduationYear());
			userAdditionalInfo.setCustomSpeciality(positionRequest.getCustomSpeciality());
			userAdditionalInfo.setCustomPosition(positionRequest.getCustomPosition());

		} else {
			userAdditionalInfo = new UserAdditionalInfo();
			if (positionRequest.getCurrentGraduation() != null) {
				userAdditionalInfo.setCurrentGraduation(degreeMapper.toDegree(positionRequest.getCurrentGraduation()));
			}

			if (positionRequest.getLastGraduationYear() != null) {
				userAdditionalInfo.setLastGraduationYear(positionRequest.getLastGraduationYear());
			}
			if (positionRequest.getLastGraduation() != null) {
				userAdditionalInfo.setLastGraduation(degreeMapper.toDegree(positionRequest.getLastGraduation()));
			}
			userAdditionalInfo.setCustomSpeciality(positionRequest.getCustomSpeciality());
			userAdditionalInfo.setCustomPosition(positionRequest.getCustomPosition());

			userAdditionalInfo.setUser(currentUser);

		}
		userAdditionalInfoService.saveAdditionInfo(userAdditionalInfo);
		informationGiverRepository.save(currentUser);
		return positionRequestMapper.toPositionDetails(userAdditionalInfo, positionRequest);

	}

	public UserBankDataResponse updateUserBankData(UserBankDataRequest bankDataDto) {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		UserBankData userBankData = userBankDataService.getUserBankData(currentUser.getUuid());
		if (userBankData != null) {
			userBankData.setBankAccountOwner(bankDataDto.getBankAccountOwner());
			userBankData.setRib(bankDataDto.getRib());
			userBankData.setBankName(bankDataDto.getBankName());
			userBankData.setBankingAgency(bankDataDto.getBankingAgency());
			userBankData.setAccountType(bankDataDto.getAccountType());
			userBankData.setNumTelD17(bankDataDto.getNumTelD17());
		} else {
			userBankData = new UserBankData();
			userBankData.setBankAccountOwner(bankDataDto.getBankAccountOwner());
			userBankData.setRib(bankDataDto.getRib());
			userBankData.setBankName(bankDataDto.getBankName());
			userBankData.setBankingAgency(bankDataDto.getBankingAgency());
			userBankData.setAccumulatedBalance(0);
			userBankData.setUnpaidAccumulatedBalance(0);
			userBankData.setAccountType(bankDataDto.getAccountType());
			userBankData.setNumTelD17(bankDataDto.getNumTelD17());

			userBankData.setUser(currentUser);

		}
		userBankDataService.saveUserBankData(userBankData);
		return userBankDataMapper.toUserBankDataResponse(userBankData);

	}

	public void initiateBankdata(UUID teacherId) {
		InformationGiver teacher = (InformationGiver) userService.getUserByUUID(teacherId);
		UserBankData userBankData = new UserBankData();
			userBankData.setAccumulatedBalance(0);
			userBankData.setUnpaidAccumulatedBalance(0);
			userBankData.setUser(teacher);
			userBankDataService.saveUserBankData(userBankData);

	}


	public PositionResponse getUserPosition() {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		return positionRequestMapper.toPositionDetailsResponse(currentUser);
	}

	public UserBankDataResponse getUserBankData() {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		return userBankDataMapper.toUserBankDataResponse(currentUser.getUuid());
	}

	public ExpertDetails getExpertInfo() {
		User currentUser = userService.getCurrentUser();
		/*boolean accountStatus = verifiedExpertService.checkCompletedValidation(currentUser.getUuid());
		return (currentUser != null
				? expertDetailsMapper.toExpertDetailsResponse((InformationGiver) currentUser, accountStatus)
				: null);
				*/
		return (currentUser != null
				? expertDetailsMapper.toExpertDetailsResponse((InformationGiver) currentUser, true)
				: null);
	}

	public ExpertDescription getExpertDescription() {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		return currentUser != null ? expertDescriptionMapper.toExpertDescription(currentUser) : null;

	}

	@Transactional
	public ExpertDescription updateExpertIntro(ExpertDescriptionRequest expertDescription) {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		currentUser.setUserDescription(expertDescription.getDescription());
		if (expertDescription.getVideoFile() != null) {
			currentUser.getMedias().add(saveMedia(currentUser, expertDescription.getVideoFile(), MediaContext.VIDEO));
		}
		if (expertDescription.getCoverPictureFile() != null) {
			currentUser.getMedias()
					.add(saveMedia(currentUser, expertDescription.getCoverPictureFile(), MediaContext.PICTURE_COVER));
		}

		InformationGiver expert = informationGiverRepository.saveAndFlush(currentUser);

		return expertDescriptionMapper.toExpertDescription(expert);
	}

	public Media saveMedia(User currentUser, MultipartFile file, MediaContext mediaContext) {

		if (!currentUser.getMedias().isEmpty()) {
			Optional<Media> picture = currentUser.getMedias().stream()
					.filter(userMedia -> userMedia.getMediaContext() == mediaContext).findFirst();
			Media pictureToDelete = picture.isPresent() ? picture.get() : null;
			if (pictureToDelete != null) {
				currentUser.getMedias().remove(pictureToDelete);
			}
		}
		Media media = null;
		try {
			media = mediaService.saveMedia(file, mediaContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return media;

	}

	public ValidationAccountResponse checkFieldsIsNull() {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		UserFees userFees = userFeesService.getUserFeesByUserUuidFeesType(currentUser.getUuid(), FeesType.REGISTRATION);
		ValidationAccountResponse validationAccountResponse = new ValidationAccountResponse();
		ExpertDetails expertDetails = getExpertInfo();
		if (this.verifiedExpertService.checkPartialValidation(expertDetails.getUserUuid())) {
			validationAccountResponse.setPartialYet(true);
		} else {

			PositionResponse positionResponse = getUserPosition();
			List<String> requiredExpertFieldNames = Arrays.asList("userFirstName", "userLastName", "userIdentityType",
					"userIdentityNumber", "userIdentityPicture", "userProfilePicture");
			List<String> requiredPositionFieldNames = new ArrayList<String>();

			if (positionResponse.getUserPosition() != null
					&& positionResponse.getUserPosition().getPositionCode().equals("ETUDIANT")) {
				requiredPositionFieldNames = new ArrayList<>(Arrays.asList("userPosition", "userSpeciality",
						"customSpeciality", "customPosition", "cvDocument", "certificateDocument", "lastGraduationYear",
						"currentGraduation", "lastGraduation"));

			} else {
				requiredPositionFieldNames = new ArrayList<>(Arrays.asList("userPosition", "userSpeciality",
						"customSpeciality", "customPosition", "cvDocument", "graduationDocument"));

			}
			// userPosition and custom postion are complementary so we have to remove the
			// null value
			if (positionResponse.getUserPosition() != null) {
				requiredPositionFieldNames.remove("customPosition");
			} else if (positionResponse.getUserPosition() == null && positionResponse.getCustomPosition() == null
					|| positionResponse.getCustomPosition() != null) {
				requiredPositionFieldNames.remove("userPosition");

			}

			if (positionResponse.getUserSpeciality() != null) {
				requiredPositionFieldNames.remove("customSpeciality");
			} else if (positionResponse.getUserSpeciality() == null && positionResponse.getCustomSpeciality() == null
					|| positionResponse.getCustomSpeciality() != null) {
				requiredPositionFieldNames.remove("userSpeciality");

			}
			List<VerifiedFieldDto> verifiedFieldResponse = new ArrayList<>();
			verifiedFieldResponse.addAll(Utils.checkFieldsIsNull(expertDetails, requiredExpertFieldNames));
			verifiedFieldResponse.addAll(Utils.checkFieldsIsNull(positionResponse, requiredPositionFieldNames));
			if (verifiedFieldResponse.stream().filter(field -> !field.isValidate()).findFirst().orElse(null) != null) {
				validationAccountResponse.setVerificationStatus(false);
			} else {
				validationAccountResponse.setVerificationStatus(true);
			}
			if (validationAccountResponse.isVerificationStatus() && userFees != null
					&& userFees.getPaymentStatus().equals(PaymentStatus.VALIDATED)) {
				verifiedExpertService.saveVerifiedExpert(verifiedExpertMapper.toVerifiedExpert(expertDetails,
						positionResponse, ValidationStatus.PARTIAL, currentUser));

			}
			validationAccountResponse.setFieldDtoList(verifiedFieldResponse);
			validationAccountResponse.setPartialYet(false);
		}
		validationAccountResponse.setPaymentStatus(userFees != null ? userFees.getPaymentStatus() : null);
		validationAccountResponse.setExemptFromFees(((userFees == null && currentUser.isExemptFromFees()) ? true
				: userFees != null ? userFees.getUser().isExemptFromFees() : false));

		return validationAccountResponse;
	}

	public List<ValidationHistoryDto> getValidationHistory() {
		InformationGiver currentUser = (InformationGiver) userService.getCurrentUser();
		List<VerifiedExpert> verifiedExperts = verifiedExpertService.findByExpertUuid(currentUser.getUuid());
		return verifiedExperts.stream().map(item -> {
			ValidationHistoryDto validationHistoryDto = new ValidationHistoryDto();
			validationHistoryDto.setValidationDate((item.getUpdatedAt()));
			validationHistoryDto.setValidationRequestDate(item.getCreatedAt());
			validationHistoryDto.setValidationStatus(item.getStatus());
			validationHistoryDto.setRefusalReason(item.getRefusalReason());
			return validationHistoryDto;
		}).collect(Collectors.toList());

	}

	List<InformationGiver> getExpertBySkillCode(String skillCode) {
		return informationGiverRepository.findBySkillsSkillCode(skillCode);
	}

	public List<ValidationExpertResponse> recoverValidationList(String validationStatus) {

		List<VerifiedExpert> data;
		if (validationStatus != null)
			data = verifiedExpertRepository.findByStatus(ValidationStatus.valueOf(validationStatus));
		else
			data = verifiedExpertRepository.findAll();
		return data.stream().sorted(Comparator.comparing(VerifiedExpert::getCreatedAt).reversed())
				.map(verifiedExpertMapper::toValidationExpertResponse).collect(Collectors.toList());

	}
	
	public List<ValidationExpertResponse> recoverTeahcerInscriptionRequests() {

		List<TeacherRegisterRequest> data = teacherRegisterRequestRepository.findAll();
		return data.stream().sorted(Comparator.comparing(TeacherRegisterRequest::getCreatedAt).reversed())
				.map(teacherRegisterRequestMapper::toValidationExpertResponse).collect(Collectors.toList());

	}

	public void confirmValidation(UUID validationUuid) {
		VerifiedExpert data = verifiedExpertRepository.findByUuid(validationUuid);
		if (data != null && data.getStatus() == ValidationStatus.PARTIAL) {
			data.setStatus(ValidationStatus.COMPLETED);
			verifiedExpertRepository.save(data);
			data.getUser().setValidate(true);
			data.getUser().setDateValidation(new Date());
			informationGiverRepository.save(data.getUser());
			// send email to user
			List<String> destination = Arrays.asList(data.getUser().getUserEmail());
			Map<String, Object> maps = new HashMap<>();
			EmailDto emailDto = new EmailDto(Constants.MAIL_SUBJECT_EXPERT_VALIDATION_ACCEPTED,
					"validate-expert-ok.html", maps, new HashMap<>(), EmailContext.NOTIF_EXPERT_VALIDATION_ACCEPTED);

			emailService.sendMail(emailDto, destination);
			// send email to all administrator
			notifAdministrators(data.getUser(), Constants.VALIDATION);
		}
	}

	public void refuseValidation(UUID validationUuid, MessageRequestDto messageRequestDto) {
		// update status
		VerifiedExpert data = verifiedExpertRepository.findByUuid(validationUuid);
		if (data != null) {
			data.setStatus(ValidationStatus.REFUSED);
			data.setRefusalReason(messageRequestDto.getMessage());
			verifiedExpertRepository.save(data);
			// send email
			List<String> destination = Arrays.asList(data.getUser().getUserEmail());
			Map<String, Object> maps = new HashMap<>();
			maps.put("validationRefuseRaison", messageRequestDto.getMessage());
			EmailDto emailDto = new EmailDto(Constants.MAIL_SUBJECT_EXPERT_VALIDATION_DECLINE,
					"validate-expert-ko.html", maps, new HashMap<>(), EmailContext.NOTIF_EXPERT_VALIDATION_REFUSED);

			emailService.sendMail(emailDto, destination);
		}
	}

	public List<ExpertResponse> getExpertList() {
		List<InformationGiver> data;
		data = informationGiverRepository.findAll();
		return data.stream().map(expertDetailsMapper::toExpertResponse).collect(Collectors.toList());
	}

	public List<InformationGiver> getAllExpert() {
		return informationGiverRepository.findAll();
	}

	public InformationGiver saveExpert(InformationGiver expert) {
		return informationGiverRepository.save(expert);
	}

	public ExpertProfileDto getExpertProfile(UUID expertUuid) {
		User currentUser = userService.getCurrentUser();
		InformationGiver expert = (InformationGiver) userService.getUserByUUID(expertUuid);
		ExpertProfileDto profileDto = expertDetailsMapper.toExpertProfileDto(expert,
				currentUser.getUuid().equals(expertUuid));
		if (!profileDto.isValidatedAccount() && !profileDto.isMyOwnAccount()) {
			return ExpertProfileDto.builder().isValidatedAccount(false).isMyOwnAccount(false).build();
		} else
			return profileDto;
	}

	public List<InformationGiver> getUserByAccountStatus(AccountStatus accountStatus) {
		return informationGiverRepository.findByAccountStatus(accountStatus);
	}

	public void registerTeacher(ExpertRegisterRequest registerRequest) throws Exception {
		TeacherRegisterRequest user = new TeacherRegisterRequest();

		InformationGiver existingTeacher = null;
		if (registerRequest.getUserUuid() != null) { // the request is from an existing teacher
			existingTeacher = informationGiverRepository.findByUuid(registerRequest.getUserUuid()).orElse(null);
			user.setExistingTeacherUuid(registerRequest.getUserUuid());
		}
		
		//if (!userService.checkUserPhoneNumber(registerRequest.getUserPhoneNumber()) && !userService.checkUserEmail(registerRequest.getUserEmail())) {
			user.setUserFirstName(registerRequest.getUserFirstName());
			user.setUserLastName(registerRequest.getUserLastName());
			user.setUserEmail(existingTeacher == null ?  registerRequest.getUserEmail() : existingTeacher.getUserEmail());
			//user.setUserPassword(encoder.encode(registerRequest.getUserPassword()));
			user.setUserPhoneNumber(registerRequest.getUserPhoneNumber());
			Role teacher = this.roleRepository.findByRoleCode(RoleCode.INFORMATION_GIVER);
			user.setUserRole(teacher);
			user.setUserRegisterDate(new Date());
			user.setIdentityType(IdentityType.CIN.toString());
			user.setIdentityNumber(registerRequest.getUserIdentityNumber());
			user.setDescription(registerRequest.getDescription());
			user.setDidYouTeach(registerRequest.getDidYouTeach());
			user.setTermsAccepted(registerRequest.isTermsAccepted());
			user.setCurrentSchool(registerRequest.getCurrentSchool());
			user.setCurrentLevel(skillAreaService.findByCode(registerRequest.getCurrentLevel()));
			user.setCurrentCourse(skillService.findByCode(registerRequest.getCurrentCourse()));
			user.setPreviousOnlineTeaching(registerRequest.getPreviousOnlineTeaching());
			user.setPreviousOnlinePlatforms(registerRequest.getPreviousOnlinePlatforms());
			user.setPreviousSupportCourses(registerRequest.getPreviousSupportCourses());
			user.setSupportCourseLevel(skillAreaService.findByCode(registerRequest.getSupportCourseLevel()));
			user.setSupportCourseCourse(skillService.findByCode(registerRequest.getSupportCourseCourse()));
			UUID uuid = teacherRegisterRequestRepository.save(user).getUuid();
			// save teacher files
			/*Media identityDocument = mediaService.saveMedia(registerRequest.getUserIdentityFile(),
					MediaContext.PICTURE_IDENTITY);
			user.getMedias().add(identityDocument);*/
			if (registerRequest.getUserPicture() != null) {
				Media picture = mediaService.saveMedia(registerRequest.getUserPicture(), MediaContext.PICTURE_PROFIL);
				user.getMedias().add(picture);
			}
			if (registerRequest.getUserDegreeFile() != null) {
				Media degree = mediaService.saveMedia(registerRequest.getUserDegreeFile(),
						MediaContext.PICTURE_GRADUATION);
				user.getMedias().add(degree);
			}

			if (registerRequest.getUserCVFile() != null) {
				Media cv = mediaService.saveMedia(registerRequest.getUserCVFile(), MediaContext.CV_DOCUMENT);
				user.getMedias().add(cv);
			}

			teacherRegisterRequestRepository.save(user);
		//}
	}
	
	public Boolean checkUserEmailForTeacherRequest(String userEmail) {
		return userRepository.findByUserEmail(userEmail).isPresent()
				|| teacherRegisterRequestRepository.findByUserEmail(userEmail).isPresent();
	}

	public ValidationExpertResponse getTeacherRequest(UUID registerRequestUuid) {
		TeacherRegisterRequest request = teacherRegisterRequestRepository.findByUuid(registerRequestUuid).orElse(null);
		if (request == null)
			return null;
		return teacherRegisterRequestMapper.toValidationExpertResponse(request);

	}
	
	public void validateTeacherRequest(UUID registerRequestUuid, String assignedCourseCode ) {
		TeacherRegisterRequest request = teacherRegisterRequestRepository.findByUuid(registerRequestUuid).orElse(null);
		if (request == null)
			return;
		InformationGiver informationGiver = teacherRegisterRequestMapper.toValidatedInformationGiver(request);
		String initialPassword = informationGiver.getUserPhoneNumber();
		if(request.getExistingTeacherUuid() == null) {
			informationGiver.setUserPassword(encoder.encode(initialPassword));
		}
		informationGiver.setElligibleToDoCourses(true);
		informationGiver.setValidate(true);

		//SkillArea assignedLevel = skillAreaService.findByCode(assignedLevelCode);
		Skill assignedCourse = skillService.findByCode(assignedCourseCode);
		
		informationGiver.getSkills().add(assignedCourse);

		informationGiver = informationGiverRepository.save(informationGiver);

		// Ajouter les informations bancaires
		initiateBankdata(informationGiver.getUuid());

		
		//supprimer le request une fois le compte est valdié
		teacherRegisterRequestRepository.delete(request);
		
		// send email to user
		List<String> destination = Arrays.asList(informationGiver.getUserEmail());
		Map<String, Object> maps = new HashMap<>();
		maps.put("expertSkillArea", assignedCourse.getSkillArea().getSkillAreaLabel());
		maps.put("expertSkill", assignedCourse.getSkillLabel());
		maps.put("isExist", request.getExistingTeacherUuid() != null);
		if (request.getExistingTeacherUuid() == null) {
			maps.put("expertPassword", initialPassword);
		}

		EmailDto emailDto = new EmailDto(Constants.MAIL_SUBJECT_EXPERT_VALIDATION_ACCEPTED, "validate-expert-ok.html",
				maps, new HashMap<>(), EmailContext.NOTIF_EXPERT_VALIDATION_ACCEPTED);

		emailService.sendMail(emailDto, destination);
		// send email to all administrator
		notifAdministrators(informationGiver, Constants.VALIDATION);
	}

	/*
	 * public PageDto<ExpertProfileDto> getExpertsPaginated( Integer pageIndex,
	 * Integer offset, List<String> skillLevels, List<String> skills,
	 * 
	 * boolean isHomeService
	 * 
	 * ) {
	 * 
	 * ExpertSearchCriteria searchCriteria = new ExpertSearchCriteria(skillLevels,
	 * skills, isHomeService); if (pageIndex <= 0) { throw new
	 * BadRequestException("page Index should be greater or equals than 1"); }
	 * 
	 * long totalElementsSize = 0l; List<InformationGiver> experts = null; if
	 * (searchCriteria .isEmptySkills() && searchCriteria.isEmptySkillLevels() &&
	 * searchCriteria.isHomeService()) { Pageable pageable =
	 * PageRequest.of(pageIndex - 1, offset, Sort.by("createdAt").ascending());
	 * Page<InformationGiver> announcementsPage =
	 * userRepository.findAll(ExpertSpecifications
	 * .createExpertsSpecifications(searchCriteria), pageable);
	 * 
	 * totalElementsSize = announcementsPage.getTotalElements(); experts =
	 * announcementsPage.getContent(); } else { experts = userRepository.findAll(
	 * ExpertSpecifications .createExpertsSpecifications(searchCriteria));
	 * 
	 * totalElementsSize = experts.size(); }
	 * 
	 * List<ExpertProfileDto> finalList = experts .stream() .map( expert -> { return
	 * expertDetailsMapper.toSearchExpertProfileDto(expert);
	 * 
	 * }) .collect(Collectors.toList());
	 * 
	 * return new PageDto<>(finalList, totalElementsSize); }
	 */

	public UserDetails getUserByUuid(UUID uuid)
	{
		InformationGiver user= informationGiverRepository.findByUuid(uuid).orElse(null);
		return user!=null? userMapper.toUserDetailsValidation(user):null;
	}

}
