package fr.imcoding.edu365;

import fr.imcoding.edu365.business.RunnerService;
import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.business.mappers.SkillAreaSectionMapper;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.dtos.SkillAreaSectionDto;
import fr.imcoding.edu365.enumeration.AccountStatus;
import fr.imcoding.edu365.persistence.entities.*;
import fr.imcoding.edu365.persistence.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import fr.imcoding.edu365.config.FileStorageProperties;
import fr.imcoding.edu365.enumeration.RoleCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	private final TeacherCourseRepository teacherCourseRepository;
	private final CityRepository cityRepository;
	private final SkillAreaRepository skillAreaRepository;
	private final SkillAreaSectionRepository skillAreaSectionRepository;
	private final SkillRepository skillRepository;
	private final PositionRepository positionRepository;
	private final SpecialityRepository specialityRepository;
	private final RunnerService runnerService;
	private final SkillAreaSectionMapper skillAreaSectionMapper;
	private final MediaRepository mediaRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;


	public static void main(String[] args) {
		SpringApplication.run(Edu365App.class, args);
		log.info("------------ The EDU 365 APP server was sucessuflly started ---");
	}

	@Override
	public void run(String... arg0) {
		String password = encoder.encode("22148600");
		System.out.println(password);
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
		//this.initiatePhysicsCoursesFromBGToMP();
		/*this.initiateGeneralChimicsCoursesFromBGToMP();
		this.initiateInorganicChimicsCoursesFromBGToMP();

		this.initiatePhysicsCoursesFromMP1ToBG1();
		this.initiateAnalysisCoursesFromMP1ToBG1();

		this.initiatePhysicsCoursesFromMP1ToBG2();

		this.initiateAnalysisCoursesFromMP1ToMPI1();

		//this.initiatePhysicsCoursesFromBGToPC();
		this.initiateGeneralChimicsCoursesFromBGToPC();
		this.initiateInorganicChimicsCoursesFromBGToPC();

		initiateAlgebreCoursesFromPCToBG();

		//this.initiateAnalyseCoursesFromBGToMP();
		//this.initiateAnalyseCoursesFromBGToPC();
		//this.initiateAlgebreCoursesFromBGToMP();
		//this.initiateAlgebreCoursesFromBGToPC();

		this.initiateAlgebreCoursesFromMPToBG();
		this.initiateAlgebreCoursesFromMPToPC();

		this.initiateAlgebreCoursesFromMPToMPI();

		//this.initiateInformatiqueCoursesFromMP1ToBG2();


		this.initiateInformatiqueCoursesFromBGToPC();
		this.initiateInformatiqueCoursesFromBGToMP();

		//this.initiateInformatiqueCoursesFromBG2ToMP1();
		//this.initiateInformatiqueCoursesFromMP2ToMP1();
		//this.initiateInformatiqueCoursesFromPC2ToMP1();
		this.initiateInformatiqueCoursesFromMP1ToPC1();

		this.initiateInformatiqueCoursesFromMP1ToMPI1();

		this.initiateInformatiqueCoursesFromMP1ToPT1();
		this.initiateChimieCoursesFromMP1ToPT1();
		this.initiatePhysicsCoursesFromMP1ToPT1();
		this.initiateAlgebreCoursesFromPC1ToPT1();
		this.initiateAnalyseCoursesFromPC1ToPT1();

		this.initiatePhysicsCoursesFromMP1ToMPI1();

		this.initiateChimieCoursesFromMP1ToPC1();
		this.initiateChimieCoursesFromMP1ToBG1();

		this.initiateChimieCoursesFromMP1ToMPI1();

		this.initiateInformatiqueCoursesFromMP2ToPT2();
		this.initiateInformatiqueCoursesFromMP2ToMPI2();
		this.initiateAlgebreCoursesFromPC2ToPT2();
		this.initiateAlgebreCoursesFromPC2ToMPI2();
		this.initiateAnalyseCoursesFromPC2ToPT2();
		this.initiateAnalyseCoursesFromPC2ToMPI2();
		this.initiatePhysicsCoursesFromMP2ToPT2();
		this.initiateChimieGeneralCoursesFromMP2ToPT2();
		this.initiateChimieInorganiqueCoursesFromMP2ToPT2();

		this.initiateChimieGeneralCoursesFromMP2ToMPI2();

		this.initiateChimieInorganiqueCoursesFromMP2ToMPI2();

		this.initiateInformatiqueCoursesFromMP1ToBG1();

		this.initiatePhysicsCoursesFromMP2ToMPI2();

		// PREPA 1 2
		this.initiatePhysicsCoursesFromMP1ToPREPA1_2_MP();
		this.initiatePhysicsCoursesFromMP2ToPREPA1_2_MP();
		this.initiatePhysicsCoursesFromPC2ToPREPA1_2_MP();
		this.initiateChimieGeneraleCoursesFromMP1ToPREPA1_2_MP();
		this.initiateChimieGeneraleCoursesFromMP2ToPREPA1_2_MP();
		this.initiateChimieInorganiqueCoursesFromMP2ToPREPA1_2_MP();
		this.initiateAlgebreCoursesFromMP1ToPREPA1_2_MP();
		this.initiateAlgebreCoursesFromMP2ToPREPA1_2_MP();
		this.initiateAnalyseCoursesFromMP1ToPREPA1_2_MP();
		this.initiateAnalyseCoursesFromMP2ToPREPA1_2_MP();
		this.initiateInformatiqueCoursesFromMP1ToPREPA1_2_MP();
		this.initiateInformatiqueCoursesFromMP2ToPREPA1_2_MP();*/





this.initAdminUser();
	}

	/*public void migrateCourses() {
		List<String> treatedCourses = new ArrayList<>();
		teacherCourseRepository.findAll().forEach(course -> {
			if(course.getSkillAreaSection() != null) {
				if(!treatedCourses.contains(course.getTitle())) {
					List<TeacherCourse> courses = teacherCourseRepository.findByTitle(course.getTitle());
					TeacherCourse teacherCourse = new TeacherCourse();

					teacherCourse.setCreator(course.getCreator());

					teacherCourse.setTitle(course.getTitle());
					teacherCourse.setDescription(course.getDescription());
					teacherCourse.setType(course.getType());
					teacherCourse.setQuarter(course.getQuarter());
					teacherCourse.setSkill(course.getSkill());
					teacherCourse.setIsPremium(course.getIsPremium());

					teacherCourse.setSkillArea(course.getSkillArea());
					teacherCourse.setSkillAreaSections(courses.stream().map(TeacherCourse::getSkillAreaSection).collect(Collectors.toList()));
					teacherCourse.setCreatedAt(courses.stream().map(TeacherCourse::getCreatedAt).min(Date::compareTo).get());
					teacherCourse.setUpdatedAt(courses.stream().map(TeacherCourse::getCreatedAt).min(Date::compareTo).get());
					teacherCourse.getMedias().addAll(course.getMedias());
					teacherCourse.setShouldBeDisplayed(true);
					teacherCourseRepository.save(teacherCourse);
					course.setShouldBeDisplayed(false);
					teacherCourseRepository.save(course);
				}
			}
		});
	}*/

	//@PostConstruct
	public void initiatePhysicsCoursesFromBGToMP() {
		log.info("Starting migration from BG2 to MP2 - Physique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill physique = skillRepository.findBySkillCode("PREPA2_PHYSICS");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mp.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiatePhysicsCoursesFromMP1ToBG1() {
		log.info("Starting migration from MP1 to BG1 - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill physique = skillRepository.findBySkillCode("PREPA1_PHYSICS");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), bg.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiatePhysicsCoursesFromMP1ToBG2() {
		log.info("Starting migration from MP1 to BG2 - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill physique = skillRepository.findBySkillCode("PREPA1_PHYSICS");
		Skill physique2 = skillRepository.findBySkillCode("PREPA2_PHYSICS");


		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), bg.getId(), physique2.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique2);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAnalysisCoursesFromMP1ToBG1() {
		log.info("Starting migration from MP1 to BG1 - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill analyse = skillRepository.findBySkillCode("PREPA1_ANALYSIS");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), bg.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalysisCoursesFromMP1ToMPI1() {
		log.info("Starting migration from MP1 to MPI1 - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill analyse = skillRepository.findBySkillCode("PREPA1_ANALYSIS");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mpi.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiatePhysicsCoursesFromBGToPC() {
		log.info("Starting migration from BG2 to PC2 - Physique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill physique = skillRepository.findBySkillCode("PREPA2_PHYSICS");

		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pc.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateGeneralChimicsCoursesFromBGToMP() {
		log.info("Starting migration from BG2 to MP2 - Chimie génerale");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill chimieGeneral = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_GENERAL");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), chimieGeneral.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mp.getId(), chimieGeneral.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateGeneralChimicsCoursesFromBGToPC() {
		log.info("Starting migration from BG2 to PC2 - Chimie génerale");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill chimieGeneral = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_GENERAL");

		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), chimieGeneral.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pc.getId(), chimieGeneral.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInorganicChimicsCoursesFromBGToMP() {
		log.info("Starting migration from BG2 to MP2 - Chimie inorganique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill chimieInorganique = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_INORGANIQUE");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), chimieInorganique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mp.getId(), chimieInorganique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInorganicChimicsCoursesFromBGToPC() {
		log.info("Starting migration from BG2 to PC2 - Chimie inorganique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill chimieInorganique = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_INORGANIQUE");

		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), chimieInorganique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pc.getId(), chimieInorganique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalyseCoursesFromBGToMP() {
		log.info("Starting migration from BG2 to MP2 - Analyse");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill analyse = skillRepository.findBySkillCode("PREPA2_ANALYSIS");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mp.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAnalyseCoursesFromBGToPC() {
		log.info("Starting migration from BG2 to PC2 - Analyse");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill analyse = skillRepository.findBySkillCode("PREPA2_ANALYSIS");

		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pc.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAlgebreCoursesFromBGToMP() {
		log.info("Starting migration from BG2 to MP2 - Algèbre");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill algebre = skillRepository.findBySkillCode("PREPA2_ALGEBRA");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mp.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAlgebreCoursesFromBGToPC() {
		log.info("Starting migration from BG2 to PC2 - Algèbre");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill algebre = skillRepository.findBySkillCode("PREPA2_ALGEBRA");

		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pc.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAlgebreCoursesFromPCToBG() {
		log.info("Starting migration from PC2 to BG2 - Algèbre");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		Skill algebre = skillRepository.findBySkillCode("PREPA2_ALGEBRA");

		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), bg.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}


	public void initiateAlgebreCoursesFromMPToBG() {
		log.info("Starting migration from MP1 to BG1 - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill algebre = skillRepository.findBySkillCode("PREPA1_ALGEBRA");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), bg.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAlgebreCoursesFromMPToPC() {
		log.info("Starting migration from MP1 to PC1 - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		Skill algebre = skillRepository.findBySkillCode("PREPA1_ALGEBRA");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pc.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAlgebreCoursesFromMPToMPI() {
		log.info("Starting migration from MP1 to MPI1 - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill algebre = skillRepository.findBySkillCode("PREPA1_ALGEBRA");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mpi.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromMP1ToPT1() {
		log.info("Starting migration from MP 1 to PT 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pt.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInformatiqueCoursesFromMP2ToPT2() {
		log.info("Starting migration from MP 2 to PT 2 - Informatique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill informatique = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pt.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInformatiqueCoursesFromMP2ToMPI2() {
		log.info("Starting migration from MP 2 to MPI 2 - Informatique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill informatique = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mpi.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAlgebreCoursesFromPC1ToPT1() {
		log.info("Starting migration from PC 1 to PT 1 - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill algebre = skillRepository.findBySkillCode("PREPA1_ALGEBRA");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), pc.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pt.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(algebre);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateAlgebreCoursesFromPC2ToPT2() {
		log.info("Starting migration from PC 2 to PT 2 - Algèbre");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill algebre = skillRepository.findBySkillCode("PREPA2_ALGEBRA");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pt.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(algebre);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAlgebreCoursesFromPC2ToMPI2() {
		log.info("Starting migration from PC 2 to MPI 2 - Algèbre");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill algebre = skillRepository.findBySkillCode("PREPA2_ALGEBRA");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mpi.getId(), algebre.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(algebre);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalyseCoursesFromPC1ToPT1() {
		log.info("Starting migration from PC 1 to PT 1 - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill analyse = skillRepository.findBySkillCode("PREPA1_ANALYSIS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), pc.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pt.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(analyse);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalyseCoursesFromPC2ToPT2() {
		log.info("Starting migration from PC 2 to PT 2 - Analyse");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill analyse = skillRepository.findBySkillCode("PREPA2_ANALYSIS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pt.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(analyse);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalyseCoursesFromPC2ToMPI2() {
		log.info("Starting migration from PC 2 to MPI 2 - Analyse");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill analyse = skillRepository.findBySkillCode("PREPA2_ANALYSIS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), analyse.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mpi.getId(), analyse.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(analyse);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initAdminUser() {
		Role adminRole = this.roleRepository.findByRoleCode(RoleCode.ADMINISTRATOR);
		if(!userRepository.findByUserEmail("admin@freeacademy.tn").isPresent()) {
			User admin = new User();
			admin.setUserEmail("admin@freeacademy.tn");
			admin.setUserPassword(encoder.encode("Freeacademy@2026!"));
			admin.setUserRole(adminRole);
			admin.setUserFirstName("Admin");
			admin.setUserLastName("Admin");
			admin.setAccountStatus(AccountStatus.ACTIVE);
			userRepository.save(admin);


		}

	}

	public void initiatePhysicsCoursesFromMP1ToPT1() {
		log.info("Starting migration from MP 1 to PT 1 - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill physique = skillRepository.findBySkillCode("PREPA1_PHYSICS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pt.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiatePhysicsCoursesFromMP1ToMPI1() {
		log.info("Starting migration from MP 1 to PT 1 - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill physique = skillRepository.findBySkillCode("PREPA1_PHYSICS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mpi.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiatePhysicsCoursesFromMP2ToPT2() {
		log.info("Starting migration from MP 2 to PT 2 - Physique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill physique = skillRepository.findBySkillCode("PREPA2_PHYSICS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pt.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiatePhysicsCoursesFromMP2ToMPI2() {
		log.info("Starting migration from MP 2 to MPI 2 - Physique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill physique = skillRepository.findBySkillCode("PREPA2_PHYSICS");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mpi.getId(), physique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieCoursesFromMP1ToPT1() {
		log.info("Starting migration from MP 1 to PT 1 - Chimie génreale");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill chimie = skillRepository.findBySkillCode("PREPA1_CHEMISTRY_GENERAL");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pt.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieCoursesFromMP1ToPC1() {
		log.info("Starting migration from MP 1 to PC 1 - Chimie génreale");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		Skill chimie = skillRepository.findBySkillCode("PREPA1_CHEMISTRY_GENERAL");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pc.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieCoursesFromMP1ToMPI1() {
		log.info("Starting migration from MP 1 to PC 1 - Chimie génreale");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill chimie = skillRepository.findBySkillCode("PREPA1_CHEMISTRY_GENERAL");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mpi.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieCoursesFromMP1ToBG1() {
		log.info("Starting migration from MP 1 to BG 1 - Chimie génreale");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill chimie = skillRepository.findBySkillCode("PREPA1_CHEMISTRY_GENERAL");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), bg.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieGeneralCoursesFromMP2ToPT2() {
		log.info("Starting migration from MP 2 to PT 2 - Chimie génerale");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill chimie = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_GENERAL");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pt.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieGeneralCoursesFromMP2ToMPI2() {
		log.info("Starting migration from MP 2 to MPI 2 - Chimie génerale");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill chimie = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_GENERAL");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mpi.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}


	public void initiateChimieInorganiqueCoursesFromMP2ToPT2() {
		log.info("Starting migration from MP 2 to PT 2 - Chimie inorganique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pt = skillAreaSectionRepository.findByCode("PT");
		Skill chimie = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_INORGANIQUE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pt.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(pt);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieInorganiqueCoursesFromMP2ToMPI2() {
		log.info("Starting migration from MP 2 to MPI 2 - Chimie inorganique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill chimie = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_INORGANIQUE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), chimie.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mpi.getId(), chimie.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimie);
				teacherCourse.setIsPremium(course.getIsPremium());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromMP1ToBG2() {
		log.info("Starting migration from MP 1 to BG 2 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");
		Skill informatiquePrepa2 = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), bg.getId(), informatiquePrepa2.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatiquePrepa2);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa2);
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromMP1ToBG1() {
		log.info("Starting migration from MP 1 to BG 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), bg.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(bg);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInformatiqueCoursesFromMP1ToPC1() {
		log.info("Starting migration from MP 1 to PC 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), pc.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromMP1ToMPI1() {
		log.info("Starting migration from MP 1 to MPI 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection mpi = skillAreaSectionRepository.findByCode("MPI");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mpi.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(mpi);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInformatiqueCoursesFromBGToPC() {
		log.info("Starting migration from BG2 to PC2 - Informatique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill informatique = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");

		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), pc.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(pc);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromBGToMP() {
		log.info("Starting migration from BG2 to MP2 - Informatique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill informatique = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");

		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa2.getId(), mp.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(course.getSkill());
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(course.getSkillArea());
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromBG2ToMP1() {
		log.info("Starting migration from BG 2 to MP 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection bg = skillAreaSectionRepository.findByCode("BG");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");
		Skill informatiquePrepa2 = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), bg.getId(), informatiquePrepa2.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mp.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}


	public void initiateInformatiqueCoursesFromMP2ToMP1() {
		log.info("Starting migration from MP 2 to MP 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");
		Skill informatiquePrepa2 = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), informatiquePrepa2.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mp.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}
	public void initiateInformatiqueCoursesFromPC2ToMP1() {
		log.info("Starting migration from PC 2 to MP 1 - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");
		Skill informatiquePrepa2 = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");


		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), informatiquePrepa2.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa1.getId(), mp.getId(), informatique.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa1);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(course.getShouldBeDisplayed());
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	//PREPA 1 2
	public void initiatePhysicsCoursesFromMP1ToPREPA1_2_MP() {
		log.info("Starting migration from MP1 to PREPA 1_2 MP - Physique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill physique = skillRepository.findBySkillCode("PREPA1_PHYSICS");
		Skill physique12 = skillRepository.findBySkillCode("PREPA12_PHYSICS");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), physique12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiatePhysicsCoursesFromMP2ToPREPA1_2_MP() {
		log.info("Starting migration from MP2 to PREPA 1_2 MP - Physique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill physique = skillRepository.findBySkillCode("PREPA2_PHYSICS");
		Skill physique12 = skillRepository.findBySkillCode("PREPA12_PHYSICS");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), physique12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiatePhysicsCoursesFromPC2ToPREPA1_2_MP() {
		log.info("Starting migration from PC2 to PREPA 1_2 MP - Physique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill physique = skillRepository.findBySkillCode("PREPA2_PHYSICS");
		Skill physique12 = skillRepository.findBySkillCode("PREPA12_PHYSICS");
		SkillAreaSection pc = skillAreaSectionRepository.findByCode("PC");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), pc.getId(), physique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), physique12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(physique12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieGeneraleCoursesFromMP1ToPREPA1_2_MP() {
		log.info("Starting migration from MP1 to PREPA 1_2 MP - Chimie generale");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill chimieGenerale = skillRepository.findBySkillCode("PREPA1_CHEMISTRY_GENERAL");
		Skill chimieGenerale12 = skillRepository.findBySkillCode("PREPA12_CHEMISTRY_GENERAL");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), chimieGenerale.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), chimieGenerale12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimieGenerale12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieGeneraleCoursesFromMP2ToPREPA1_2_MP() {
		log.info("Starting migration from MP2 to PREPA 1_2 MP - Chimie generale");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill chimieGenerale = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_GENERAL");
		Skill chimieGenerale12 = skillRepository.findBySkillCode("PREPA12_CHEMISTRY_GENERAL");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), chimieGenerale.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), chimieGenerale12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimieGenerale12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateChimieInorganiqueCoursesFromMP2ToPREPA1_2_MP() {
		log.info("Starting migration from MP2 to PREPA 1_2 MP - Chimie inorganique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill chimieInorganique = skillRepository.findBySkillCode("PREPA2_CHEMISTRY_INORGANIQUE");
		Skill chimieInorganique12 = skillRepository.findBySkillCode("PREPA12_CHEMISTRY_INORGANIQUE");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), chimieInorganique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), chimieInorganique12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(chimieInorganique12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAlgebreCoursesFromMP1ToPREPA1_2_MP() {
		log.info("Starting migration from MP1 to PREPA 1_2 MP - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill algebre = skillRepository.findBySkillCode("PREPA1_ALGEBRA");
		Skill algebre12 = skillRepository.findBySkillCode("PREPA12_ALGEBRA");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), algebre12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(algebre12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAlgebreCoursesFromMP2ToPREPA1_2_MP() {
		log.info("Starting migration from MP2 to PREPA 1_2 MP - Algèbre");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill algebre = skillRepository.findBySkillCode("PREPA2_ALGEBRA");
		Skill algebre12 = skillRepository.findBySkillCode("PREPA12_ALGEBRA");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), algebre.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), algebre12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(algebre12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalyseCoursesFromMP1ToPREPA1_2_MP() {
		log.info("Starting migration from MP1 to PREPA 1_2 MP - Analyse");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill analysis = skillRepository.findBySkillCode("PREPA1_ANALYSIS");
		Skill analysis12 = skillRepository.findBySkillCode("PREPA12_ANALYSIS");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), analysis.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), analysis12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(analysis12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateAnalyseCoursesFromMP2ToPREPA1_2_MP() {
		log.info("Starting migration from MP2 to PREPA 1_2 MP - Analyse");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill analysis = skillRepository.findBySkillCode("PREPA2_ANALYSIS");
		Skill analysis12 = skillRepository.findBySkillCode("PREPA12_ANALYSIS");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), analysis.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), analysis12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(analysis12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromMP1ToPREPA1_2_MP() {
		log.info("Starting migration from MP1 to PREPA 1_2 MP - Informatique");
		SkillArea prepa1 = skillAreaRepository.findBySkillAreaCode("PREPA1");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill informatique = skillRepository.findBySkillCode("PREPA1_COMPUTER_SCIENCE");
		Skill informatique12 = skillRepository.findBySkillCode("PREPA12_COMPUTER_SCIENCE");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa1.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), informatique12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}

	public void initiateInformatiqueCoursesFromMP2ToPREPA1_2_MP() {
		log.info("Starting migration from MP2 to PREPA 1_2 MP - Informatique");
		SkillArea prepa2 = skillAreaRepository.findBySkillAreaCode("PREPA2");
		SkillArea prepa12 = skillAreaRepository.findBySkillAreaCode("PREPA12");
		Skill informatique = skillRepository.findBySkillCode("PREPA2_COMPUTER_SCIENCE");
		Skill informatique12 = skillRepository.findBySkillCode("PREPA12_COMPUTER_SCIENCE");
		SkillAreaSection mp = skillAreaSectionRepository.findByCode("MP");

		List<TeacherCourse> courses = teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(prepa2.getId(), mp.getId(), informatique.getId());
		courses.forEach(course -> {
			if(teacherCourseRepository.findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(prepa12.getId(), mp.getId(), informatique12.getId(), course.getTitle()).isEmpty()) {
				TeacherCourse teacherCourse = new TeacherCourse();

				teacherCourse.setCreator(course.getCreator());

				teacherCourse.setTitle(course.getTitle());
				teacherCourse.setDescription(course.getDescription());
				teacherCourse.setType(course.getType());
				teacherCourse.setQuarter(course.getQuarter());
				teacherCourse.setSkill(informatique12);
				teacherCourse.setIsPremium(course.getIsPremium());

				teacherCourse.setSkillArea(prepa12);
				teacherCourse.setSkillAreaSection(mp);
				teacherCourse.setCreatedAt(course.getCreatedAt());
				teacherCourse.setUpdatedAt(course.getUpdatedAt());
				List<Media> medias = course.getMedias().stream().map(media -> {
					Media newMedia = new Media();
					newMedia.setMediaContext(media.getMediaContext());
					newMedia.setMediaLabel(media.getMediaLabel());
					newMedia.setMediaSize(media.getMediaSize());
					newMedia.setMediaUrl(media.getMediaUrl());
					newMedia.setMediaContentType(media.getMediaContentType());
					newMedia.setOriginalName(media.getOriginalName());
					return newMedia;
				}).collect(Collectors.toList());
				medias = mediaRepository.saveAll(medias);
				teacherCourse.setMedias(medias);
				//teacherCourse.getMedias().addAll(course.getMedias());
				teacherCourse.setShouldBeDisplayed(true);
				teacherCourseRepository.save(teacherCourse);
			}

		});

	}



}