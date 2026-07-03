package org.gan.assistant.controller;

import jakarta.validation.Valid;
import org.gan.assistant.dto.ScheduleRequest;
import org.gan.assistant.dto.ScheduleResponse;
import org.gan.assistant.entity.Schedule;
import org.gan.assistant.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleRequest request){
        ScheduleResponse schedule=scheduleService.createSchedule(request);
        return ResponseEntity.ok(schedule);
    }
    @GetMapping("/today")
    public ResponseEntity<?> getTodaySchedules(){
        List<Schedule> schedules=scheduleService.getTodaySchedules();
        return ResponseEntity.ok(schedules);
    }
    //查询所有行程
    @GetMapping
    public ResponseEntity<?> getAllSchedules(){
        List<Schedule> schedules=scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    //查询单个行程
    @GetMapping("/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable Long id){
        Schedule schedule=scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    //修改行程
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id,@Valid @RequestBody ScheduleRequest request){
        Schedule schedule=scheduleService.updateSchedelu(id,request);
        return ResponseEntity.ok(schedule);
    }

    //删除行程
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id){
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("删除成功");
    }
}
