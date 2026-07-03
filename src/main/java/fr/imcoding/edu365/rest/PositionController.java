package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.mappers.PositionMapper;
import fr.imcoding.edu365.business.services.PositionService;
import fr.imcoding.edu365.dtos.PositionDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 10/06/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/position")
@RequiredArgsConstructor
public class PositionController {

  private final PositionService positionService;
  private final PositionMapper positionMapper;


  @GetMapping()
  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  public List<PositionDto> getAllPositions() {
    return this.positionService.getAllPositions().stream().map(position->positionMapper.toPositionDto(position)).collect(Collectors.toList());
  }



}
