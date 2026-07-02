package org.gan.assistant.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;//事件标题

    private String description;//描述

    @Column(name = "star_time",nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    //关联用户:多对一
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
    }
}
