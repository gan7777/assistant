package org.gan.assistant.repository;

import org.gan.assistant.entity.Schedule;
import org.gan.assistant.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository  extends JpaRepository<Schedule,Long> {

    List<Schedule> findByUserAndStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Schedule> findByUserOrderByStartTimeAsc(User user);


    //分页查询(支持标题搜索)
    @Query(value = "SELECT * FROM schedules WHERE user_id = :user_id " +
            "AND (CAST(:keyword AS text) IS NULL OR title LIKE CONCAT('%', :keyword, '%')) " +
            "AND (CAST(:start_date AS timestamp) IS NULL OR star_time >= :start_date) " +
            "AND (CAST(:end_date AS timestamp) IS NULL OR star_time <= :end_date) " +
            "ORDER BY star_time ASC",
            countQuery = "SELECT COUNT(*) FROM schedules WHERE user_id = :user_id " +
                    "AND (CAST(:keyword AS text) IS NULL OR title LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (CAST(:start_date AS timestamp) IS NULL OR star_time >= :start_date) " +
                    "AND (CAST(:end_date AS timestamp) IS NULL OR star_time <= :end_date)",
            nativeQuery = true)
    Page<Schedule> findSchedulesByUserWithFilters(
            @Param("user_id") Long userId,
            @Param("keyword") String keyword,
            @Param("start_date") LocalDateTime startDate,
            @Param("end_date") LocalDateTime endDate,
            Pageable pageable);
}
