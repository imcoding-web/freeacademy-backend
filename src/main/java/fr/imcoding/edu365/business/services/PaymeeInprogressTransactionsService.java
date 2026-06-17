package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.*;
import fr.imcoding.edu365.persistence.repositories.PaymeeInprogressTransactionsRepository;
import fr.imcoding.edu365.persistence.repositories.SobflousInprogressTransactionsRepository;
import fr.imcoding.edu365.utils.Utils;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 03/12/2022
 */
@Service
@AllArgsConstructor
public class PaymeeInprogressTransactionsService {
  private final UserService userService;
  private final PaymeeInprogressTransactionsRepository paymeeInprogressTransactionsRepository;

  public PaymeeInProgressTransaction savePaymeeTransaction(Offer offer, SkillAreaPackage pack, String token) {
    PaymeeInProgressTransaction inprogressTransactions=new PaymeeInProgressTransaction();
      inprogressTransactions.setOffer(offer);
      inprogressTransactions.setPack(pack);
    inprogressTransactions.setToken(token);
    inprogressTransactions.setTransactionType(offer != null ? TransactionType.OFFER: TransactionType.PACKAGE);
    User user =  userService.getCurrentUser();
    inprogressTransactions.setUser(user);
    return paymeeInprogressTransactionsRepository.save(inprogressTransactions);
  }

  public PaymeeInProgressTransaction saveFeePaymeeTransaction(Integer amount,String token) {
    InformationGiver user=(InformationGiver)userService.getCurrentUser();
    PaymeeInProgressTransaction inprogressTransactions=new PaymeeInProgressTransaction();
    inprogressTransactions.setUser(user);
    inprogressTransactions.setToken(token);
    inprogressTransactions.setTransactionType(TransactionType.USER_FEES);
    return paymeeInprogressTransactionsRepository.save(inprogressTransactions);
  }

  public PaymeeInProgressTransaction getByToken(String token){
    return paymeeInprogressTransactionsRepository.findByToken(token).orElse(null);
  }


}
