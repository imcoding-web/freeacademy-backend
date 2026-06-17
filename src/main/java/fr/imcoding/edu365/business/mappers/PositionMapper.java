package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.PositionDto;
import fr.imcoding.edu365.persistence.entities.Position;
import fr.imcoding.edu365.persistence.repositories.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */
@Component
@RequiredArgsConstructor
public class PositionMapper {

  private final PositionRepository positionRepository;


  public Position toPosition(PositionDto position) {
    return positionRepository.findByUuid(position.getPositionUuid());
  }

  public PositionDto toPositionDto(Position position) {
    return new PositionDto(position.getUuid(), position.getPositionLabel(),
        position.getPositionCode());
  }

}
