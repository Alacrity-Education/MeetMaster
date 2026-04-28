package ro.alacrity.meetmaster.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.alacrity.meetmaster.domain.MeetRepository;

import java.time.Instant;

@Component
public class MeetCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(MeetCleanupJob.class);
    private static final long EXPIRY_SECONDS = 8 * 3600L; // 8 hours

    private final MeetRepository meetRepository;

    public MeetCleanupJob(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void deactivateExpiredMeets() {
        long cutoff = Instant.now().getEpochSecond() - EXPIRY_SECONDS;
        int count = meetRepository.deactivateExpiredMeets(cutoff);
        if (count > 0) {
            log.info("Deactivated {} expired meeting(s)", count);
        }
    }
}
