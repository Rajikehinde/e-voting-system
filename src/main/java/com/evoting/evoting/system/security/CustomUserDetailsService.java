package com.evoting.evoting.system.security;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.repository.AdministrationRepository;
import com.evoting.evoting.system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdministrationRepository administrationRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administration admin = administrationRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with provided credentials not found!" + username));
        Set<GrantedAuthority> authorities = admin.getRole().stream()
                .map((role)-> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        return new User(admin.getUsername(),admin.getPassword(),authorities);
    }
}
