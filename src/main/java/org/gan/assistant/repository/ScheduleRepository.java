package org.gan.assistant.repository;

import org.gan.assistant.entity.Schedule;
import org.gan.assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository  extends JpaRepository<Schedule,Long> {

    List<Schedule> findByUserAndStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Schedule> findByUserOrderByStartTimeAsc(User user,LocalDateTime start,LocalDateTime end);

}
