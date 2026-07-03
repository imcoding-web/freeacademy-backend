package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.Offer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
  Offer findByUuid(UUID uuid);

  List<Offer> findByAnnouncementUuidOrderByOfferStatusDesc(UUID uuid);
  List<Offer> findByAnnouncementUuidAndOfferGiverUuidAndOfferStatusNot(UUID announcementUuid,UUID expertUuid,OfferStatus offerStatus);

  List<Offer> findByOfferGiverUuidAndOfferStatusNot(UUID uuid,OfferStatus status);
  List<Offer> findByAnnouncementAnnouncementPublisherAndOfferStatusNot(InformationSeeker informationSeeker,OfferStatus status);
  List<Offer> findByOfferGiverUuidOrderByCreatedAtDesc(UUID uuid);
  List<Offer> findByAnnouncementAnnouncementPublisherUuid(UUID uuid);
  List<Offer> findByOfferStatus(OfferStatus offerStatus);
  Offer findByUniqueIdentifier(String id);






}
