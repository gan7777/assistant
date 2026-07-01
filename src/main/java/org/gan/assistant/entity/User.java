package org.gan.assistant.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false,length = 50)
    private String username;


    @Column(unique = true,nullable = true,length = 100)
    private String  email;

    @Column(nullable = false)
    private String password; //加密后的密码

    @Column(name = "create_at")
    private LocalDateTime createdAt;


    @Column(name = "update_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected  void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
    }



}
