package com.evoting.evoting.system.security;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.repository.AdministrationRepository;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.RoleRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdministrationRepository administrationRepository;
    @Autowired
    private VotersRepository votersRepository;
    @Autowired
    private CandidatesRepository candidatesRepository;
    @Autowired
    private RoleRepository roleRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Administration admin = administrationRepository.findByUsername(username)
//                .orElseThrow(()-> new UsernameNotFoundException("User with provided credentials not found!" + username));
//        Candidate candidate = candidatesRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with provided credentials not found!" + username));
//        Voter voter = votersRepository.findByUsername(username)
//                .orElseThrow(()-> new UsernameNotFoundException("User with provided credentials not found!" + username));
//
//        Set<GrantedAuthority> authorities = admin.getRole().stream()
//                .map((role)-> new SimpleGrantedAuthority(role.getRoleName()))
//                .collect(Collectors.toSet());
//
//        return new User(admin.getUsername(),admin.getPassword(),authorities);
//
//    }
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Check if the user exists in the Administration repository
    Optional<Administration> adminOptional = administrationRepository.findByUsername(username);
    if (adminOptional.isPresent()) {
        Administration admin = adminOptional.get();
        Set<GrantedAuthority> authorities = admin.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }

    // Check if the user exists in the Candidate repository
    Optional<Candidate> candidateOptional = candidatesRepository.findByUsername(username);
    if (candidateOptional.isPresent()) {
        Candidate candidate = candidateOptional.get();
        Set<GrantedAuthority> authorities = candidate.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
        return new User(candidate.getUsername(), candidate.getPassword(), authorities);
    }

    // Check if the user exists in the Voter repository
    Optional<Voter> voterOptional = votersRepository.findByUsername(username);
    if (voterOptional.isPresent()) {
        Voter voter = voterOptional.get();
        Set<GrantedAuthority> authorities = voter.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
        return new User(voter.getUsername(), voter.getPassword(), authorities);
    }

    throw new UsernameNotFoundException("User with provided credentials not found: " +username);
}
}
