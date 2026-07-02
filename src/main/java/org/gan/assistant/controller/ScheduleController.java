package org.gan.assistant.controller;

import org.gan.assistant.dto.ScheduleRequest;
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
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleRequest request){
        Schedule schedule=scheduleService.createSchedule(request);
        return ResponseEntity.ok(schedule);
    }
    @GetMapping("/today")
    public ResponseEntity<?> getTodaySchedules(){
        List<Schedule> schedules=scheduleService.getTodaySchedules();
        return ResponseEntity.ok(schedules);
    }
}
