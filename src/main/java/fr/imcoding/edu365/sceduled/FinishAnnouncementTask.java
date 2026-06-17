package fr.imcoding.edu365.sceduled;

import fr.imcoding.edu365.business.services.AnnouncementService;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.persistence.entities.Announcement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 19/11/2022
 */
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class FinishAnnouncementTask {
  private final AnnouncementService announcementService;
  @Scheduled(cron = "${edu365.announcement.scheduler.automatic.finish.past.announcement.cron}")
  public void finishAnnouncement(){
    log.info("***START AUTOMATIC FINISH PAST ANNOUNCEMENTS ***");

    List<Announcement> announcementList=announcementService.getPastAnnouncement();
announcementList.stream().forEach(announcement -> {
  announcement.setAnnouncementStatus(AnnouncementStatus.FINISHED);
  announcementService.updateAnnouncementStatus(announcement,AnnouncementStatus.FINISHED);
});

    log.info("***END AUTOMATIC FINISH PAST ANNOUNCEMENTS ***");

  }

}
