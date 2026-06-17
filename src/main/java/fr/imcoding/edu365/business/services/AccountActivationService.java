package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.exceptions.NotFoundException;
import fr.imcoding.edu365.persistence.entities.AccountActivation;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.AccountActivationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountActivationService {

  private final AccountActivationRepository accountActivationRepository;

  public AccountActivation getByAccountActivationCode(String activationCode) {
    log.info("Get AccountActivation with UUID: {}", activationCode);
    return accountActivationRepository.findByActivationCode(activationCode)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Aucun compte lié à ce code " + activationCode + " a été trouvé"));
  }


  public String saveAccountActivation(User user) {
    AccountActivation accountActivation = new AccountActivation();
    accountActivation.setUser(user);
    return accountActivationRepository.save(accountActivation).getActivationCode();
  }


  public void removeAccountActication(AccountActivation accountActivation) {
    log.info("Remove AccountActivation with UUID: {}", accountActivation.getUuid());
    accountActivationRepository.delete(accountActivation);
  }


}
