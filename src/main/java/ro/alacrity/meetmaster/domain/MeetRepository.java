package ro.alacrity.meetmaster.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface MeetRepository extends JpaRepository<Meet, String> {

    List<Meet> findAllByOrderByCreateTimeDesc();

    List<Meet> findByCreateTimeGreaterThanEqualOrderByCreateTimeDesc(long since);

    @Modifying
    @Query("UPDATE Meet m SET m.active = false WHERE m.active = true AND m.createTime < :cutoff")
    int deactivateExpiredMeets(@Param("cutoff") long cutoff);

    default String generateUniqueId() {
        String id;
        do {
            id = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
        } while (existsById(id));
        return id;
    }
}
