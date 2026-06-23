package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.PositionMapper;
import fr.imcoding.edu365.business.mappers.SpecialityMapper;
import fr.imcoding.edu365.dtos.PositionDto;
import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.persistence.entities.Position;
import fr.imcoding.edu365.persistence.repositories.PositionRepository;
import java.util.List;
import java.util.UUID;
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
public class PositionService {
  private final PositionRepository positionRepository;
  private final PositionMapper positionMapper;


  public List<Position> getAllPositions() {
    return this.positionRepository.findAll((Sort.by(Sort.Direction.ASC, "positionLabel")));
  }

  public Position savePosition(Position position) {
    return this.positionRepository.save(position);
  }

  public Position getByCOde(String positionCode) {
    return this.positionRepository.findByPositionCodeOrderByPositionLabel(positionCode);
  }

  public PositionDto getByUuid(UUID positionUuid) {
    return positionMapper.toPositionDto(this.positionRepository.findByUuid(positionUuid));
  }

  public void deletePosition(Long id) {
    this.positionRepository.deleteById(id);
  }


}
