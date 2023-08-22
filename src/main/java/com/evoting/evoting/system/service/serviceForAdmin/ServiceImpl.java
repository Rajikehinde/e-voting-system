package com.evoting.evoting.system.service.serviceForAdmin;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Role;
import com.evoting.evoting.system.dto.request.AdminRequest;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.repository.AdministrationRepository;
import com.evoting.evoting.system.repository.RoleRepository;
import com.evoting.evoting.system.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ServiceImpl implements com.evoting.evoting.system.service.serviceForAdmin.Service {
    @Autowired
    private final AdministrationRepository administrationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ServiceImpl(AdministrationRepository administrationRepository) {
        this.administrationRepository = administrationRepository;
    }

    @Override
    public Response registerAdmin(AdminRequest adminRequest) {
        Boolean isExist = administrationRepository.existsByEmail(adminRequest.getEmail());
        if (isExist){
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        Administration administration = Administration.builder()
                .firstName(adminRequest.getFirstName())
                .middleName(adminRequest.getMiddleName())
                .lastName(adminRequest.getLastName())
                .email(adminRequest.getEmail())
                .username(adminRequest.getUsername())
                .password(passwordEncoder.encode(adminRequest.getPassword()))
                .phoneNumber(adminRequest.getPhoneNumber())
                .dateOfBirth(adminRequest.getDateOfBirth())
                .status(true)
                .build();

        Role role = roleRepository.findByRoleName("ROLE_ADMIN").get();
        log.info("give me the role" + role);
        administration.setRole(Collections.singleton(role));

        Administration savedAdmin = administrationRepository.save(administration);
        return Response.builder()
                .code(ResponseUtils.USER_REGISTER_CODE)
                .message(ResponseUtils.USER_REGISTER_MESSAGE)
                .data(Data.builder()
                        .name(savedAdmin.getFirstName() + " " + savedAdmin.getMiddleName() + " " + savedAdmin.getLastName())
                        .build())
                .build();
    }

    @Override
    public List<Response> fetchAllAdmin() {
        List<Administration> administrationList = administrationRepository.findAll();

        //response on all the customers and there details
        List<Response> responseList = new ArrayList<>();
        for (Administration admin : administrationList) {
            responseList.add(Response.builder()
                    .code(ResponseUtils.USER_LIST_CODE)
                    .message(ResponseUtils.USER_LIST_MESSAGE)
                    .data(Data.builder()
                            .name(admin.getFirstName() + " " + admin.getMiddleName() + " " + admin.getLastName())
                            .build())
                    .build());
        }
        return responseList;
    }

    @Override
    public Response updateAdmin(AdminRequest adminRequest) {
        Boolean isExist = administrationRepository.existsByEmail(adminRequest.getEmail());
        if (!isExist){
            return Response.builder()
                    .code(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        Administration administration = administrationRepository.findByUsername(adminRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                        administration.setFirstName(adminRequest.getFirstName());
                        administration.setMiddleName(adminRequest.getMiddleName());
                        administration.setLastName(adminRequest.getLastName());
                        administration.setEmail(adminRequest.getEmail());
                        administration.setUsername(adminRequest.getUsername());
                        administration.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
                        administration.setPhoneNumber(adminRequest.getPhoneNumber());
                        administration.setDateOfBirth(adminRequest.getDateOfBirth());

        Administration savedAdmin = administrationRepository.save(administration);
        return Response.builder()
                .code(ResponseUtils.USER_PROFILE_UPDATE_CODE)
                .message(ResponseUtils.USER_PROFILE_UPDATE_MESSAGE)
                .data(Data.builder()
                        .name(savedAdmin.getFirstName() + " " + savedAdmin.getMiddleName() + " " + savedAdmin.getLastName())
                        .build())
                .build();
    }

    @Override
    public Response delete(Long id) {
        boolean isExist = administrationRepository.existsById(id);
        if (!isExist){
             return Response.builder()
                    .message(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        Optional<Administration> admin = administrationRepository.findById(id);
        admin.get().setDeleteStatus(true);
        Administration saveAdmin = administrationRepository.save(admin.get());

        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }
}
