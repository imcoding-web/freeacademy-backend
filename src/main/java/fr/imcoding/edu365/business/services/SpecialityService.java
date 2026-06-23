package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.SpecialityMapper;
import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.persistence.entities.Speciality;
import fr.imcoding.edu365.persistence.repositories.SpecialityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */
@Service
@RequiredArgsConstructor
public class SpecialityService {

  private final SpecialityRepository specialityRepository;
  private final SpecialityMapper specialityMapper;


public List<SpecialityDto> getSpecialities() {
  return this.specialityRepository.findAll(Sort.by(Sort.Direction.ASC, "specialityLabel")).stream()
      .map(speciality -> specialityMapper.toSpecialityDto(speciality)).collect(Collectors
          .toList());

}


  public Speciality getSpecialiteByCode(String positionCode) {
    return this.specialityRepository.findBySpecialityCodeOrderBySpecialityLabel(positionCode);
  }

  public Speciality saveSpeciality(Speciality speciality) {
    return this.specialityRepository.save(speciality);
  }
}
