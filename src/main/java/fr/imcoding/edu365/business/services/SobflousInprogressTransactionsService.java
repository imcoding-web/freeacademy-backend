package fr.imcoding.edu365.business.services;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.SobflousInprogressTransaction;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.SobflousInprogressTransactionsRepository;
import fr.imcoding.edu365.utils.Utils;
import lombok.AllArgsConstructor;

/**
 * @author Rokaya
 * @Date 03/12/2022
 */
@Service
@AllArgsConstructor
public class SobflousInprogressTransactionsService {
  private final SobflousInprogressTransactionsRepository inprogressTransactionsRepository;

  public SobflousInprogressTransaction saveSobflousTransaction(Offer offer) {
    SobflousInprogressTransaction inprogressTransactions=new SobflousInprogressTransaction();
      inprogressTransactions.setOffer(offer);
    inprogressTransactions.setTransmid(Utils.getSaltString());
    inprogressTransactions.setTransactionType(TransactionType.OFFER);
    return inprogressTransactionsRepository.save(inprogressTransactions);
  }

public SobflousInprogressTransaction getByTransmId(String transmId){
    return inprogressTransactionsRepository.findByTransmid(transmId).orElse(null);
}

  public SobflousInprogressTransaction saveSobflousFeeTransaction(User user) {

    SobflousInprogressTransaction inprogressTransactions=new SobflousInprogressTransaction();
    inprogressTransactions.setUser(user);
    inprogressTransactions.setTransmid(Utils.getSaltString());
    inprogressTransactions.setTransactionType(TransactionType.USER_FEES);
    return inprogressTransactionsRepository.save(inprogressTransactions);
  }
}
