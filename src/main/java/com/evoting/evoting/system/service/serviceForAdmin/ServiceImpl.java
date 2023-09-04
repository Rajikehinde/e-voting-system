package com.evoting.evoting.system.service.serviceForAdmin;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Role;
import com.evoting.evoting.system.dto.request.AdminRequest;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.email.emailDto.EmailDetails;
import com.evoting.evoting.system.email.emailService.EmailService;
import com.evoting.evoting.system.otpMailing.OtpService;
import com.evoting.evoting.system.repository.AdministrationRepository;
import com.evoting.evoting.system.repository.RoleRepository;
import com.evoting.evoting.system.utils.ResponseUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
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
    EmailService emailService;
    @Autowired
    private OtpService otpService;
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
    public Response registerAdmin(AdminRequest adminRequest) throws UnirestException {
        //checking if admin already existed in my database by email
        Boolean isExist = administrationRepository.existsByEmail(adminRequest.getEmail());
        if (isExist){
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        //creating the admin if not existed already in the database
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

        //appending a role of security to the admin
        Role role = roleRepository.findByRoleName("ROLE_ADMIN").get();
        log.info("give me the role" + role);
        administration.setRole(Collections.singleton(role));

        //saving the created admin in the database
        Administration savedAdmin = administrationRepository.save(administration);
        //implementation of OTP
//        otpService.sendOtpTrial(savedAdmin.getPhoneNumber());

        //appending email to the created admin
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedAdmin.getEmail())
                .subject("Admin")
                .messageBody("This user successfully registered as the admin.\n" +
                        "Admin Name: " + savedAdmin.getFirstName() + " " + savedAdmin.getMiddleName() + " " + savedAdmin.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);
        //returning a response to the created admin
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
        //checking if admin already existed in my database by email
        Boolean isExist = administrationRepository.existsByEmail(adminRequest.getEmail());
        if (!isExist){
            return Response.builder()
                    .code(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        //finding the existed admin in the database and updating it
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

                        //saving the update in the database
        Administration savedAdmin = administrationRepository.save(administration);

        //appending email to the updated admin
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedAdmin.getEmail())
                .subject("Admin")
                .messageBody("This profile successfully updated.\n" +
                        "Admin Name: " + savedAdmin.getFirstName() + " " + savedAdmin.getMiddleName() + " " + savedAdmin.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);

        //returning response to the updated admin
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
        //checking if the admin is existed in the database by id
        boolean isExist = administrationRepository.existsById(id);
        if (!isExist){
             return Response.builder()
                    .message(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        //finding the admin in the database by id
        Optional<Administration> admin = administrationRepository.findById(id);
        //setting the delete status of the found admin to true
        admin.get().setDeleteStatus(true);
        //saving the deleted admin in the database
        Administration saveAdmin = administrationRepository.save(admin.get());

        //returning response of the deleted admin
        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }
}
