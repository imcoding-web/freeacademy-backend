package fr.imcoding.edu365.business;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.services.AnnouncementService;
import fr.imcoding.edu365.business.services.InformationGiverService;
import fr.imcoding.edu365.business.services.VerifiedExpertService;
import fr.imcoding.edu365.utils.AnnouncementUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 01/02/2023
 */
@Service
@RequiredArgsConstructor
public class RunnerService {
	private final VerifiedExpertService verifiedExpertService;
	private final InformationGiverService informationGiverService;
	private final AnnouncementService announcementService;

	public void setValidateStatus() {
		informationGiverService.getAllExpert().stream().forEach(expert -> {
			boolean accountStatus = verifiedExpertService.checkCompletedValidation(expert.getUuid());
			if (accountStatus) {
				expert.setExemptFromFees(true);
				expert.setValidate(true);
				informationGiverService.saveExpert(expert);
			}

		});

	}

	public void setEstimatedPriceToPayForAnnouncement() {
		announcementService.getAllAnnouncements().stream()
				//.filter(announcement -> announcement.getEstimatedPriceToPay() == 0 || announcement.getEstimatedHourNumber() == 0)
				.forEach(announcement -> {
					announcement.setEstimatedPriceToPay(AnnouncementUtils.getEstimatedPriceToPay(
							announcement.getAnnouncementType(), announcement.getAnnouncementSkillArea()));
					announcement.setEstimatedHourNumber(AnnouncementUtils.getEstimatedHours(announcement.getAnnouncementType()));
					announcementService.save(announcement);
				});
	}

}
