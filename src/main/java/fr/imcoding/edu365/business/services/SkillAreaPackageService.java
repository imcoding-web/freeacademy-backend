package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.SkillAreaPackageMapper;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.dtos.SkillAreaPackageRequest;
import fr.imcoding.edu365.dtos.SkillAreaWithPackagesDTO;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import fr.imcoding.edu365.persistence.repositories.SkillAreaPackageRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */
@Service
@RequiredArgsConstructor
public class SkillAreaPackageService {

  private final SkillAreaPackageRepository skillAreaPackageRepository;
  private final SkillAreaService skillAreaService;
  private final SkillAreaPackageMapper skillAreaPackageMapper;


  public SkillAreaPackage addSkillAreaPackage(String skillAreaCode,SkillAreaPackageRequest skillAreaPackageRequest){
    SkillArea skillArea=skillAreaService.findByCode(skillAreaCode);
    SkillAreaPackage skillAreaPackage=new SkillAreaPackage();
    if(skillAreaPackageRequest.getUuid()==null){

      skillAreaPackage.setPackagePrice(skillAreaPackageRequest.getPackagePrice());
      skillAreaPackage.setPackageType(skillAreaPackageRequest.getPackageType());
      skillAreaPackage.setSkillLevel(skillArea);
    }else{
     skillAreaPackage=skillAreaPackageRepository.findBySkillLevelUuidAndPackageType(skillArea.getUuid(),skillAreaPackageRequest.getPackageType()).orElse(null);
      skillAreaPackage.setPackagePrice(skillAreaPackageRequest.getPackagePrice());
    }
   return skillAreaPackageRepository.save(skillAreaPackage);

  }
  public List<SkillAreaWithPackagesDTO> getAllSkillAreasWithPackages() {
    List<SkillAreaDto> skillAreas = skillAreaService.getAllSkillArea();
    List<SkillAreaPackage> skillAreaPackages = skillAreaPackageRepository.findAll();
    List<SkillAreaWithPackagesDTO> result = skillAreas.stream().map(skillArea -> {
      SkillAreaWithPackagesDTO dto = new SkillAreaWithPackagesDTO();
      dto.setSkillArea(skillArea);
      List<SkillAreaPackageRequest> skillAreaPackageList=
     (skillAreaPackages.stream()
          .filter(skillAreaPack -> skillAreaPack.getSkillLevel().getSkillAreaCode().equals(skillArea.getSkillAreaCode()))
                    .collect(Collectors.toList()).stream().map(item->{return skillAreaPackageMapper.toSkillAreaPackageRequest(item);}).collect(Collectors.toList()));
      dto.setSkillAreaPackages(skillAreaPackageList);
      return dto;
    }).collect(Collectors.toList());

    return result;
  }

  public List<SkillAreaPackageRequest> getAllPackages(){
    return this.skillAreaPackageRepository.findAll().stream().map(skillAreaPackage -> skillAreaPackageMapper.toSkillAreaPackageRequest(skillAreaPackage)).collect(Collectors.toList());
  }

  public SkillAreaPackage getPackBySkillLevelAndType(UUID skillAreaUuid,PackageType packageType){
    return skillAreaPackageRepository.findBySkillLevelUuidAndPackageType(skillAreaUuid,packageType).orElse(null);
  }
  public List<SkillAreaPackageRequest> getAllPackagesBySkillLevel(String skillAreaCode){
    return this.skillAreaPackageRepository.findBySkillLevelSkillAreaCode(skillAreaCode.toUpperCase()).stream().map(skillAreaPackage -> skillAreaPackageMapper.toSkillAreaPackageRequest(skillAreaPackage)).collect(Collectors.toList());
  }

}
