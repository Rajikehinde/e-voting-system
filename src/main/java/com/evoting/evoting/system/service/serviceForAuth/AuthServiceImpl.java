package com.evoting.evoting.system.service.serviceForAuth;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.dto.Login;
import com.evoting.evoting.system.repository.AdministrationRepository;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.VotersRepository;
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
public class AuthServiceImpl implements AuthService{

    private final AdministrationRepository administrationRepository;
    private final VotersRepository votersRepository;
    private final CandidatesRepository candidatesRepository;
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    public AuthServiceImpl(AdministrationRepository administrationRepository, VotersRepository votersRepository, CandidatesRepository candidatesRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.administrationRepository = administrationRepository;
        this.votersRepository = votersRepository;
        this.candidatesRepository = candidatesRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public AuthResponse login(Login loginDto) {
        boolean isAdminExists = administrationRepository.existsByUsername(loginDto.getUsername());
        boolean isVoterExists = votersRepository.existsByUsername(loginDto.getUsername());
        boolean isCandidateExists = candidatesRepository.existsByUsername(loginDto.getUsername());
        Authentication authentication;
        AuthResponse authResponse;
        if (isAdminExists) {
        Administration admin = administrationRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("username not found"));
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            authResponse = new AuthResponse();
            authResponse.setToken(jwtTokenProvider.generateToken(authentication));
        } else if (isVoterExists) {
        Voter voter = votersRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("userName not found"));
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(voter.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            authResponse = new AuthResponse();
            authResponse.setToken(jwtTokenProvider.generateToken(authentication));
        }else{
            Candidate candidate = candidatesRepository.findByUsername(loginDto.getUsername()).orElseThrow(()-> new UsernameNotFoundException("userName not found"));
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(candidate.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        authResponse = new AuthResponse();
        authResponse.setToken(jwtTokenProvider.generateToken(authentication));
        }
            return authResponse;
    }
    }
