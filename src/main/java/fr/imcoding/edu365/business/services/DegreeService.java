package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.DegreeMapper;
import fr.imcoding.edu365.dtos.DegreeDto;
import fr.imcoding.edu365.persistence.entities.Degree;
import fr.imcoding.edu365.persistence.repositories.DegreeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 25/09/2022
 */
@Service
@RequiredArgsConstructor
public class DegreeService {

  private final DegreeRepository degreeRepository;
  private final DegreeMapper degreeMapper;

  public Degree getDegreeByCode(String degreeCode) {
    return this.degreeRepository.findByDegreeCode(degreeCode);
  }

  public List<DegreeDto> getAllDegree() {
    return degreeRepository.findAll(Sort.by(Sort.Direction.ASC, "degreeLabel")).stream()
        .map(degree -> degreeMapper.todegreeDto(degree)).collect(Collectors.toList());
  }

  public Degree saveDegree(String degreeLabel){
    Degree degree=new Degree();
    degree.setDegreeLabel(degreeLabel);
    return degreeRepository.save(degree);

  }

}
