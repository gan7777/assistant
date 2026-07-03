package org.gan.assistant.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class SchedulePageRequest {

    private Integer page=0;//默认第0页
    private Integer size=10;//默认每页10条
    private String keyword;//搜索关键词
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;//开始日期
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;//结束时间
}
