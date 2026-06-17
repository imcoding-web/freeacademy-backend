package fr.imcoding.edu365;

import fr.imcoding.edu365.business.RunnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import fr.imcoding.edu365.config.FileStorageProperties;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.Country;
import fr.imcoding.edu365.persistence.entities.Role;
import fr.imcoding.edu365.persistence.repositories.CityRepository;
import fr.imcoding.edu365.persistence.repositories.CountryRepository;
import fr.imcoding.edu365.persistence.repositories.PositionRepository;
import fr.imcoding.edu365.persistence.repositories.RoleRepository;
import fr.imcoding.edu365.persistence.repositories.SkillAreaRepository;
import fr.imcoding.edu365.persistence.repositories.SkillRepository;
import fr.imcoding.edu365.persistence.repositories.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
@EnableFeignClients
@EnableConfigurationProperties({ FileStorageProperties.class })
public class Edu365App implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final CountryRepository countryRepository;
	private final CityRepository cityRepository;
	private final SkillAreaRepository skillAreaRepository;
	private final SkillRepository skillRepository;
	private final PositionRepository positionRepository;
	private final SpecialityRepository specialityRepository;
	private final RunnerService runnerService;


	public static void main(String[] args) {
		SpringApplication.run(Edu365App.class, args);
		log.info("------------ The EDU 365 APP server was sucessuflly started ---");
	}

	@Override
	public void run(String... arg0) {
		//runnerService.setValidateStatus();
		//runnerService.setEstimatedPriceToPayForAnnouncement();
		log.info("------------ PROCESS TO EXECUTE WHEN STARTING THE SERVER  ---");
		Role adminRole = this.roleRepository.findByRoleCode(RoleCode.ADMINISTRATOR);
		if (adminRole == null) {
			adminRole = new Role();
			adminRole.setRoleCode(RoleCode.ADMINISTRATOR);
			adminRole.setRoleLabel("ADMINISTRATEUR");
			this.roleRepository.save(adminRole);
		}
		Role lodgerRole = this.roleRepository.findByRoleCode(RoleCode.INFORMATION_GIVER);
		if (lodgerRole == null) {
			lodgerRole = new Role();
			lodgerRole.setRoleCode(RoleCode.INFORMATION_GIVER);
			lodgerRole.setRoleLabel("INFORMATION GIVER");
			this.roleRepository.save(lodgerRole);
		}
		Role ownerRole = this.roleRepository.findByRoleCode(RoleCode.INFORMATION_SEEKER);
		if (ownerRole == null) {
			ownerRole = new Role();
			ownerRole.setRoleCode(RoleCode.INFORMATION_SEEKER);
			ownerRole.setRoleLabel("INFORMATION SEEKER");
			this.roleRepository.save(ownerRole);
		}

		Country tnCountry = this.countryRepository.findByCountryCode("TN");
		if (tnCountry == null) {
			tnCountry = new Country();
			tnCountry.setCountryCode("TN");
			tnCountry.setCountryLabel("Tunisie");
			tnCountry = this.countryRepository.save(tnCountry);
		}

	}
}
