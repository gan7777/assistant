package org.gan.assistant.utils;


import org.gan.assistant.entity.User;
import org.gan.assistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    //获取当前登录用户(从Security 上下文取出用户名再去查数据库)
    public User getCurrentUser(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("当前用户不存在"));

    }

}
