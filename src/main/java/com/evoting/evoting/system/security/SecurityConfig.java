package com.evoting.evoting.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private UserDetails userDetails;
    public SecurityConfig(UserDetails userDetails){
        this.userDetails = userDetails;
    }
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

//                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement((sessionManagement)-> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize)->
                                authorize
                                        .requestMatchers(HttpMethod.POST,"/api/register/Admin").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/api/admin/login").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/api/fetchAllAdmin").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.PUT,"/api/admin/update").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE,"/api/delete/Admin").hasAuthority("ROLE_ADMIN")

                                        .requestMatchers(HttpMethod.POST,"/api/candidate/registration").permitAll()
//                                        .requestMatchers(HttpMethod.POST,"/api/admin/login").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/api/fetchAllCandidates").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.PUT,"/api/candidate/update").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE,"/api/deleteCandidate").hasAuthority("ROLE_ADMIN")

                                        .requestMatchers(HttpMethod.POST,"/api/voter/registration").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/api/fetchVoters").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.PUT,"/api/updateVoter").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE,"/api/delete/voter").hasAuthority("ROLE_ADMIN")

                                        .requestMatchers(HttpMethod.POST,"/api/presidency/vote").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/api/governor/vote").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/api/senate/vote").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/api/houseOfAssembly/vote").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/api/houseOfRep/vote").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/api/presidential/result").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/api/governorship/result").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/api/rep/result").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/api/senatorial/result").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/api/assembly/result").hasAuthority("ROLE_ADMIN")
                                        .anyRequest().authenticated()


                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .authenticationProvider(daoAuthenticationProvider())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

}
