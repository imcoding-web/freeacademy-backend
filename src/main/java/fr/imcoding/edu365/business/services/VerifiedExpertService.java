package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.VerifiedExpertMapper;
import fr.imcoding.edu365.client.dtos.response.ValidationExpertResponse;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.dtos.ValidationHistoryDto;
import fr.imcoding.edu365.enumeration.ValidationStatus;
import fr.imcoding.edu365.persistence.entities.VerifiedExpert;
import fr.imcoding.edu365.persistence.repositories.VerifiedExpertRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 17/07/2022
 */
@Service
@RequiredArgsConstructor
public class VerifiedExpertService {
  public final VerifiedExpertRepository verifiedExpertRepository;
  public final VerifiedExpertMapper verifiedExpertMapper;

  public VerifiedExpert saveVerifiedExpert(VerifiedExpert verifiedExpert){
    return verifiedExpertRepository.save(verifiedExpert);
  }

  public List<VerifiedExpert> findByExpertUuid(UUID expertUuid){
    return verifiedExpertRepository.findByUserUuid(expertUuid);
  }

  public boolean checkPartialValidation(UUID expertUuid){
   Optional<VerifiedExpert> verifiedExpert= Optional.ofNullable(
       findByExpertUuid(expertUuid).stream()
           .filter(item -> item.getStatus().equals(ValidationStatus.PARTIAL)).findFirst()
           .orElse(null));
  if(verifiedExpert.isPresent()){
    return true;
  }
  else return false;
  }
  public boolean checkCompletedValidation(UUID expertUuid){
    Optional<VerifiedExpert> completedValidation= Optional.ofNullable(
        findByExpertUuid(expertUuid).stream()
            .filter(item->item.getStatus().equals(ValidationStatus.COMPLETED)).findFirst()
            .orElse(null));
    if(completedValidation.isPresent()){
      return true;
    }
    else return false;
  }
  public VerifiedExpert findByUuid(UUID uuid){
    return verifiedExpertRepository.findByUuid(uuid);
  }

  public PageDto<ValidationExpertResponse> getExpertList( Integer page, Integer offset) {
    List <ValidationExpertResponse> expertList=new ArrayList<>();
    int pageindex=page <= 0? 0 :page-1;
    Pageable pageable = PageRequest.of(pageindex, offset, Sort.by("createdAt").ascending());
    Page<VerifiedExpert> data;
    data = verifiedExpertRepository.findAll(pageable);
    if(data.hasContent())
      expertList= data.getContent().stream().map(verifiedExpertMapper::toExpertValidationResponse).collect(Collectors
          .toList()).stream().map(expert->{
            expert.setValidatedAccount(this.checkCompletedValidation(expert.getUserUuid()));
            return expert;
          }).collect(Collectors
          .toList());
             //

    return new PageDto<>(expertList, Long.valueOf(expertList.size())) ;

  }
}
