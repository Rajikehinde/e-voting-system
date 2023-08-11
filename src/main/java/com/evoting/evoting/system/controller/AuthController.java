package com.evoting.evoting.system.controller;

import com.evoting.evoting.system.dto.Login;
import com.evoting.evoting.system.service.serviceForAuth.AuthResponse;
import com.evoting.evoting.system.service.serviceForAuth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService service;
    @PostMapping("/admin/login")
    public AuthResponse login (@RequestBody Login loginDto){
        return service.login(loginDto);
    }
}
