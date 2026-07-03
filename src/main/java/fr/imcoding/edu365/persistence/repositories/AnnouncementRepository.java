package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.persistence.entities.Announcement;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long>,JpaSpecificationExecutor<Announcement> {

  List<Announcement> findTop12ByOrderByCreatedAtDesc();

  List<Announcement> findByAnnouncementPublisherUuid(UUID userUuid);

  Announcement findByUuid(UUID uuid);
  Page<Announcement> findByAnnouncementStatus(AnnouncementStatus valueOf, Pageable pageable);

  List<Announcement> findByAnnouncementStatusNotAndAnnouncementEndAvailableDateBefore(AnnouncementStatus announcementStatus,Date date);
}
