package org.gan.assistant.service;

import org.gan.assistant.dto.ScheduleRequest;
import org.gan.assistant.entity.Schedule;
import org.gan.assistant.entity.User;
import org.gan.assistant.repository.ScheduleRepository;
import org.gan.assistant.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SecurityUtils securityUtils;

    //创建行程
    public Schedule createSchedule(ScheduleRequest request){
        User currentUser=securityUtils.getCurrentUser();

        Schedule schedule=new Schedule();
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setUser(currentUser);

        return  scheduleRepository.save(schedule);
    }
    //查询今日行程
    public List<Schedule> getTodaySchedules(){
        User currentUser=securityUtils.getCurrentUser();
        LocalDateTime startOfDay= LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay=LocalDate.now().atTime(LocalTime.MAX);

        return scheduleRepository.findByUserAndStartTimeBetween(currentUser,startOfDay,endOfDay);
    }
}
