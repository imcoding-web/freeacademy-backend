package fr.imcoding.edu365.business.services;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.persistence.entities.StudyPackage;
import fr.imcoding.edu365.persistence.repositories.StudyPackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyPackageService {

	private final StudyPackageRepository studyPackageRepository;

	public void saveStudyPackage(StudyPackage studyPackage) {
		studyPackageRepository.save(studyPackage);

	}

}
