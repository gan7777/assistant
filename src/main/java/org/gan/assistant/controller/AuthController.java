package org.gan.assistant.controller;


import jakarta.validation.Valid;
import org.gan.assistant.dto.AuthResponse;
import org.gan.assistant.dto.LoginRequest;
import org.gan.assistant.dto.RegisterRequest;
import org.gan.assistant.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register( @Valid @RequestBody RegisterRequest request){
        try{
            String message=authService.register(request);
            return ResponseEntity.ok(new AuthResponse(null,request.getUsername(),message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null,null,e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            String token=authService.login(request);
            return ResponseEntity.ok(new AuthResponse(token,request.getUsername(),"登录成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null,null,e.getMessage()));
        }
    }
}
