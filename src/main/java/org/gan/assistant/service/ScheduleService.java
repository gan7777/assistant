package org.gan.assistant.service;

import org.gan.assistant.dto.ScheduleRequest;
import org.gan.assistant.dto.ScheduleResponse;
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
    public ScheduleResponse createSchedule(ScheduleRequest request){
        User currentUser=securityUtils.getCurrentUser();

        Schedule schedule=new Schedule();
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setUser(currentUser);

        Schedule saved=scheduleRepository.save(schedule);
        return  toResponse(saved);
    }
    //查询今日行程
    public List<Schedule> getTodaySchedules(){
        User currentUser=securityUtils.getCurrentUser();
        LocalDateTime startOfDay= LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay=LocalDate.now().atTime(LocalTime.MAX);

        return scheduleRepository.findByUserAndStartTimeBetween(currentUser,startOfDay,endOfDay);
    }
    //查询所有行程(按时间排序)
    public List<Schedule> getAllSchedules(){
        User currentUser=securityUtils.getCurrentUser();
        return scheduleRepository.findByUserOrderByStartTimeAsc(currentUser);
    }
    //根据ID查询单个行程(并校验归属)
    public  Schedule getScheduleById(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("行程不存在"));
        if (!schedule.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("无权访问该行程");

        }
        return schedule;

    }
    //修改行程
    public Schedule updateSchedelu(Long id,ScheduleRequest request){
        User currentUser=securityUtils.getCurrentUser();
        Schedule schedule=scheduleRepository.findById(id).orElseThrow(()->new RuntimeException("行程不存在"));
        if(!schedule.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("无权修改该行程");
        }
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        return scheduleRepository.save(schedule);
    }
    //删除行程
    public void deleteSchedule(Long id){
        User currentUser=securityUtils.getCurrentUser();
        Schedule schedule=scheduleRepository.findById(id).orElseThrow(()->new RuntimeException("行程不存在"));
        if(!schedule.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("无权删除该行程");
        }
        scheduleRepository.delete(schedule);
    }

    public ScheduleResponse toResponse(Schedule schedule){
        ScheduleResponse response=new ScheduleResponse();
        response.setId(schedule.getId());
        response.setTitle(schedule.getTitle());
        response.setDescription(schedule.getDescription());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setCreatedAt(schedule.getCreatedAt());
        response.setUpdatedAt(schedule.getUpdatedAt());
        if(schedule.getUser()!=null){
            response.setUserId(schedule.getUser().getId());
            response.setUsername(schedule.getUser().getUsername());
        }
        return response;
    }

}
