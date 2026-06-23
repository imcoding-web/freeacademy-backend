package fr.imcoding.edu365.utils;

import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.persistence.entities.SkillArea;

public class AnnouncementUtils {

	private static final int HOUR_PRICE_PRIMARY_LEVEL = 5;
	private static final int HOUR_PRICE_BASIC_LEVEL = 10;
	private static final int HOUR_PRICE_NINTH_LEVEL = 10;
	private static final int HOUR_PRICE_SECONDARY_LEVEL = 20;
	private static final int HOUR_PRICE_BAC_LEVEL = 20;
	private static final int HOUR_PRICE_PREPA_LEVEL = 25;
	private static final int HOUR_PRICE_UNIVERSITY_LEVEL = 25;

	public static int getEstimatedPriceToPay(AnnouncementType announcementType, SkillArea skillArea) {
		switch (skillArea.getSkillAreaCode()) {
		case "P1":
		case "P2":
		case "P3":
		case "P4":
		case "P5":
		case "P6":
			if (announcementType == AnnouncementType.COURSE_EXPLANATION || announcementType == AnnouncementType.EXAM)
				return HOUR_PRICE_PRIMARY_LEVEL * 4;
			if (announcementType == AnnouncementType.EXERCICE)
				return HOUR_PRICE_PRIMARY_LEVEL;
			else
				return HOUR_PRICE_PRIMARY_LEVEL*2;
		case "P7":
		case "P8":
			if (announcementType == AnnouncementType.COURSE_EXPLANATION || announcementType == AnnouncementType.EXAM || announcementType == AnnouncementType.WRITING_EXERCICE)
				return HOUR_PRICE_BASIC_LEVEL * 2;
			else
				return HOUR_PRICE_BASIC_LEVEL;

		case "B9":
			if (announcementType == AnnouncementType.COURSE_EXPLANATION || announcementType == AnnouncementType.EXAM
					|| announcementType == AnnouncementType.WRITING_EXERCICE)
				return HOUR_PRICE_NINTH_LEVEL * 2;
			else
				return HOUR_PRICE_NINTH_LEVEL;
		case "S1":
		case "S2":
		case "S3":
			if (announcementType == AnnouncementType.EXAM || announcementType == AnnouncementType.WRITING_EXERCICE)
				return HOUR_PRICE_SECONDARY_LEVEL * 2;
			else
				return HOUR_PRICE_SECONDARY_LEVEL;
		case "BAC":
			if (announcementType == AnnouncementType.EXAM || announcementType == AnnouncementType.WRITING_EXERCICE)
				return HOUR_PRICE_BAC_LEVEL * 2;
			else
				return HOUR_PRICE_BAC_LEVEL;
		case "PREPA1":
		case "PREPA2":
			if (announcementType == AnnouncementType.EXAM || announcementType == AnnouncementType.WRITING_EXERCICE
					|| announcementType == AnnouncementType.TP)
				return HOUR_PRICE_PREPA_LEVEL * 2;
			else
				return HOUR_PRICE_PREPA_LEVEL;
		default:
			if (announcementType == AnnouncementType.EXAM || announcementType == AnnouncementType.WRITING_EXERCICE
			|| announcementType == AnnouncementType.TP)
		return HOUR_PRICE_UNIVERSITY_LEVEL * 2;
	else
		return HOUR_PRICE_UNIVERSITY_LEVEL;
		}

	}

	public static int getEstimatedHours(AnnouncementType announcementType) {
		if (announcementType != null) {
			switch (announcementType) {
			case COURSE_EXPLANATION:
			case EXAM:
			case EXAM_ASSISTANCE:
			case WRITING_EXERCICE:
			case TP:
				return 2;
			default:
				return 1;

			}

		}
		return 1;
	}

}
