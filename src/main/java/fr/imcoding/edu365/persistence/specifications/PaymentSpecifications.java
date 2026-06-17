package fr.imcoding.edu365.persistence.specifications;

import fr.imcoding.edu365.dtos.PaymentSearchRequest;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.Payment;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Rokaya
 * @Date 08/12/2022
 */
public class PaymentSpecifications {
  public static Specification<Payment> createAnnouncementSpecifications(PaymentSearchRequest searchCriteria) {

    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Join<Payment, Offer> paymentOfferJoin = root.join("offer");
      Join<Offer,InformationGiver> offerExpertJoin = paymentOfferJoin.join("offerGiver", JoinType.INNER);
      if (searchCriteria.getExpertFirstName()!=null) {
        predicates.add(builder.equal(offerExpertJoin.get("userFirstName").as(String.class),searchCriteria.getExpertFirstName()));
        predicates.add(builder.equal(offerExpertJoin.get("userLastName").as(String.class),searchCriteria.getExpertLastName()));

      }

      if (searchCriteria.getPaymentType()!=null) {
        predicates.add(builder.equal(root.get("paymentType").as(String.class),searchCriteria.getPaymentType()));
      }


      if(searchCriteria.getStartDate()!=null && !searchCriteria.getStartDate().isEmpty()) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
          Date date = formatter.parse(searchCriteria.getStartDate());
          predicates.add(builder
              .greaterThanOrEqualTo(root.get("createdAt").as(Date.class), date));
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      if(searchCriteria.getEndDate()!=null && !searchCriteria.getEndDate().isEmpty()) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
          Date date = formatter.parse(searchCriteria.getEndDate());
          predicates.add(builder
              .lessThanOrEqualTo(root.get("createdAt").as(Date.class), date));
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

}
