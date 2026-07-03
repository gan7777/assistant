package org.gan.assistant.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleResponse {

       private Long id;
       private String title;
       private String description;
       private LocalDateTime startTime;
       private LocalDateTime endTime;
       private LocalDateTime createdAt;
       private LocalDateTime updatedAt;
       private  Long userId;
       private String username;
}
