package com.evoting.evoting.system.service.serviceForAuth;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.dto.Login;
import com.evoting.evoting.system.repository.AdministrationRepository;
import com.evoting.evoting.system.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    private AdministrationRepository administrationRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    public AuthServiceImpl(AdministrationRepository administrationRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.administrationRepository = administrationRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public AuthResponse login(Login loginDto) {
        Administration admin = administrationRepository.findByUsername(loginDto.getUsername()).orElseThrow(()-> new UsernameNotFoundException("userName not found"));
        log.info("found the customer " + admin.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(),loginDto.getPassword()));
        log.info("Authenticate the customer " + admin);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtTokenProvider.generateToken(authentication));
        return authResponse;
    }
}
