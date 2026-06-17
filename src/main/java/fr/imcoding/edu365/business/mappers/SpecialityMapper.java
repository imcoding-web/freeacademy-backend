package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.persistence.entities.Speciality;
import fr.imcoding.edu365.persistence.repositories.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */

@Component
@RequiredArgsConstructor
public class SpecialityMapper {
  private final SpecialityRepository specialityRepository;


  public Speciality toSpeciality(SpecialityDto specialityRequest) {
    return specialityRepository.findByUuid(specialityRequest.getSpecialityUuid());
  }

  public SpecialityDto toSpecialityDto(Speciality speciality) {
    return new SpecialityDto(speciality.getUuid(), speciality.getSpecialityLabel(), speciality.getSpecialityCode());
  }


}
