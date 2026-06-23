package fr.imcoding.edu365.business.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.persistence.entities.InformationSeekerPackage;
import fr.imcoding.edu365.persistence.repositories.InformationSeekerPackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InformationSeekerPackageService {

	private final InformationSeekerPackageRepository informationSeekerPackageRepository;

	public InformationSeekerPackage findLastStudyPackageForAInformationGiver(UUID informationSeekerUuid) {
		return informationSeekerPackageRepository
				.findTopByPackageRelatedStudentUuidOrderByCreatedAtDesc(informationSeekerUuid);
	}

}
