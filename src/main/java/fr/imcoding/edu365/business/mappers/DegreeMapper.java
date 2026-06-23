package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.DegreeDto;
import fr.imcoding.edu365.persistence.entities.Degree;
import fr.imcoding.edu365.persistence.repositories.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 26/09/2022
 */
@Component
@RequiredArgsConstructor
public class DegreeMapper {
  private final DegreeRepository degreeRepository;


  public Degree toDegree(DegreeDto degree) {
    return degreeRepository.findByDegreeCode(degree.getDegreeCode());
  }

  public DegreeDto todegreeDto(Degree degree) {
    return new DegreeDto(degree.getUuid(), degree.getDegreeLabel(),
        degree.getDegreeCode());
  }

}
