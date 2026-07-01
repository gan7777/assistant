package org.gan.assistant.service;

import org.gan.assistant.dto.LoginRequest;
import org.gan.assistant.dto.RegisterRequest;
import org.gan.assistant.entity.User;
import org.gan.assistant.repository.UserRepository;
import org.gan.assistant.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    //注册
    public  String register(RegisterRequest request){
        //检查用户名是否存在
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("用户名已被占用");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("邮箱已被注册");
        }

        User user=new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "注册成功!";
    }

   //登录
    public String login(LoginRequest request){
        User user=userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new RuntimeException("用户名或密码错误"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("用户名或密码错误");
        }
        return jwtUtils.generateToken(user.getUsername());
    }
}
