package com.orakuma.rogator.scheduling;

import com.orakuma.rogator.application.ApplicationRepository;
import com.orakuma.rogator.application.ApplicationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ApplicationJobs {
  private final ApplicationRepository applicationRepository;
  private final int durabilityInCreationStatus;

  public ApplicationJobs(
      ApplicationRepository applicationRepository,
      @Value("${app.application.creation.durability}") int durabilityInCreationStatus) {
    this.applicationRepository = applicationRepository;
    this.durabilityInCreationStatus = durabilityInCreationStatus;
  }

  @Transactional
  @Scheduled(cron = "${cron.change-application-status}")
  public void processApplicationStatus() {
    Duration duration = Duration.ofMinutes(durabilityInCreationStatus);
    LocalDateTime cutoffTime = LocalDateTime.now().minus(duration);
    int cancelledApplications =
        applicationRepository.updateStatusForExpiredApplications(
            ApplicationStatus.CREATED, ApplicationStatus.CANCELLED, cutoffTime);
    log.info("{} applications get status updated to cancelled due inactivities.", cancelledApplications);
  }
}
