package com.evoting.evoting.system.service.serviceForAuth;

import com.evoting.evoting.system.dto.Login;

public interface AuthService {
    public AuthResponse login (Login loginDto);
}
