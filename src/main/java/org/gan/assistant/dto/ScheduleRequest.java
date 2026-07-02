package org.gan.assistant.dto;


import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ScheduleRequest {

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
