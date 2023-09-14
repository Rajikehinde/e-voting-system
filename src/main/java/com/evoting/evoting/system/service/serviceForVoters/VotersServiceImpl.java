package com.evoting.evoting.system.service.serviceForVoters;

import com.evoting.evoting.system.domain.Election;
import com.evoting.evoting.system.domain.Role;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.domain.enmPackage.Gender;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.dto.request.VotersRequest;
import com.evoting.evoting.system.email.emailDto.EmailDetails;
import com.evoting.evoting.system.email.emailService.EmailService;
import com.evoting.evoting.system.otpMailing.OtpService;
import com.evoting.evoting.system.repository.ElectionRepository;
import com.evoting.evoting.system.repository.RoleRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import com.evoting.evoting.system.utils.ResponseUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class VotersServiceImpl implements VotersService {
    private final Random random = new Random();

    @Autowired
    EmailService emailService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final   VotersRepository votersRepository;
    private final ElectionRepository electionRepository;
    private final OtpService otpService;

    public VotersServiceImpl(VotersRepository votersRepository, ElectionRepository electionRepository, OtpService otpService) {
        this.votersRepository = votersRepository;
        this.electionRepository = electionRepository;
        this.otpService = otpService;
    }

    @Override
    public Response registerVoters(VotersRequest votersRequest) throws UnirestException {
        // Checking if voter exists in the database  through email
        Boolean isExist = votersRepository.existsByEmail(votersRequest.getEmail());
        if (isExist) {
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        // Extract the initials from the last name
        String lastName = votersRequest.getLastName();
        String initials = extractInitials(lastName);

        // Generate a card number using the extracted initials
        String cardNumber = generateCardNumber(initials);

        // Calculate age based on the provided date of birth
        Date dateOfBirth =votersRequest.getDateOfBirth();
        int age = calculateAge(dateOfBirth);

        // Check if the voter is 18 years or less
        if (age < 18) {
            return Response.builder()
                    .code(ResponseUtils.AGE_LIMIT_EXCEEDED_CODE)
                    .message(ResponseUtils.AGE_LIMIT_EXCEEDED_MESSAGE)
                    .data(null)
                    .build();
        }

        // Create a voter in the database with the generated card number
        Voter voters = Voter.builder()
                .firstName(votersRequest.getFirstName())
                .middleName(votersRequest.getMiddleName())
                .lastName(votersRequest.getLastName())
                .email(votersRequest.getEmail())
                .cardNo(cardNumber)
                .username(votersRequest.getUsername())
                .password(passwordEncoder.encode(votersRequest.getPassword()))
                .gender(Gender.valueOf(votersRequest.getGender()))
                .phoneNumber(votersRequest.getPhoneNumber())
                .dateOfBirth(votersRequest.getDateOfBirth())
                .address(votersRequest.getAddress())
                .state(votersRequest.getState())
                .localGovernment(votersRequest.getLocalGovernment())
                .build();

        //appending a role of security to the admin
        Role role = roleRepository.findByRoleName("ROLE_VOTER").get();
//        log.info("give me the role" + role);
        voters.setRole(Collections.singleton(role));
        // Save the voter to the database
        Voter savedVoters=votersRepository.save(voters);



    //implementation of Otp
//        otpService.sendOtpTrial(savedVoters.getPhoneNumber());
        String emailMessage = "Welcome to e-voting system,\n\n"
                + "You have successfully created your account. Please find your account details below:\n\n"
                + "Your account name is: " + voters.getFirstName() + " " + voters.getLastName() + "\n"
                + "Your username is: " + voters.getUsername() + "\n"
                + "Please click the following link to verify your account:\n"
//                + "http://localhost:8080/api/verifyAccount/"+generateVerificationToken() + "\n\n"
                + "Thank you for joining us!\n\n"
                + "Best regards,\n"
                + "electoral commission";

        //appending email response to the account
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Voter")
                .recipient(savedVoters.getEmail())
                .messageBody(emailMessage)
                .cardNo(savedVoters.getCardNo())
                .build();
        emailService.sendSimpleEmail(emailDetails);

        //returning a response to the created voter
        return Response.builder()
                .code(ResponseUtils.USER_REGISTER_CODE)
                .message(ResponseUtils.USER_REGISTER_MESSAGE)
                .data(Data.builder()
                        .name(savedVoters.getFirstName() + " "
                                + savedVoters.getMiddleName() + " "
                                + savedVoters.getLastName()
                                )
                        .cardNo(savedVoters.getCardNo())
                        .build())
                .build();
    }

    @Override
    public List<Response> fetchAllVoters() {
        //finding all voters in database
        List<Voter> voters = votersRepository.findAll();

        //response on all the customers and there details
        List<Response> responseList = new ArrayList<>();
        for (Voter voter : voters) {
            responseList.add(Response.builder()
                    .code(ResponseUtils.USER_LIST_CODE)
                    .message(ResponseUtils.USER_LIST_MESSAGE)
                    .data(Data.builder()
                            .name(voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName())
                            .build())
                    .build());
        }
        return responseList;
    }

    @Override
    public Response updateVoters(VotersRequest votersRequest) {

        //checking if voter exists in database
        Boolean isExists = votersRepository.existsByEmail(votersRequest.getEmail());
        if (!isExists) {
            return Response.builder()
                    .code(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        //finding the existed voter in database
        Voter voter = votersRepository.findByEmail(votersRequest.getEmail());
        //updating voter in database
        voter.setFirstName(votersRequest.getFirstName());
        voter.setMiddleName(votersRequest.getMiddleName());
        voter.setLastName(votersRequest.getLastName());
        voter.setEmail(votersRequest.getEmail());
        voter.setUsername(votersRequest.getUsername());
        voter.setPassword(passwordEncoder.encode(votersRequest.getPassword()));
        voter.setDateOfBirth(votersRequest.getDateOfBirth());
        voter.setPhoneNumber(votersRequest.getPhoneNumber());
        voter.setGender(Gender.valueOf(votersRequest.getGender()));
        voter.setAddress(votersRequest.getAddress());
        voter.setLocalGovernment(votersRequest.getLocalGovernment());
        voter.setState(votersRequest.getState());

        //saving updated voter in database
        Voter savedVotes = votersRepository.save(voter);

        //appending email response to the account
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Voter")
                .recipient(savedVotes.getEmail())
                .messageBody("Voter profile updated \n" +
                        "Voter Name: " + savedVotes.getFirstName() + " " + savedVotes.getMiddleName() + " " + savedVotes.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);
        //returning a response to updated voter
        return Response.builder()
                .code(ResponseUtils.USER_PROFILE_UPDATE_CODE)
                .message(ResponseUtils.USER_PROFILE_UPDATE_MESSAGE)
                .data(Data.builder()
                        .name(savedVotes.getFirstName() + " " + savedVotes.getMiddleName()
                                + " " + savedVotes.getLastName())
                        .cardNo(savedVotes.getCardNo())
                        .build())
                .build();
    }

    @Override
    public Response delete(Long id) {
        //checking if voter exists  by id
        boolean isExist = votersRepository.existsById(id);
        if (!isExist) {
            return Response.builder()
                    .message(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        //finding voter by id
        Optional<Voter> voter = votersRepository.findById(id);
        //setting the delete status to true
        voter.get().setDeleteStatus(true);
        //saving the deleted voter to database
        Voter saveVoters = votersRepository.save(voter.get());
        //returning a response of a deleted voter
        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }




    //Link token verification
//    public static String generateVerificationToken() {
//        int tokenLength = 20; // Adjust the token length as needed
//        byte[] randomBytes = new byte[tokenLength];
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.nextBytes(randomBytes);
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
//    }

        // Method to extract initial first three letters from the last name
        public static String extractInitials(String lastName) {
            return lastName.substring(0, Math.min(lastName.length(), 3)).toUpperCase();
        }

        // Method to generate a random card 5 numbers using initials
        public static String generateCardNumber(String initials) {
            Random random = new Random();
            StringBuilder cardNumberBuilder = new StringBuilder(initials);
            for (int i = 0; i < 5; i++) {
                cardNumberBuilder.append(random.nextInt(10)); // Append random digits
            }
            return cardNumberBuilder.toString();
        }
        //The method working for date calculation for users under the age of 18
    private int calculateAge(Date dateOfBirth) {
        LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}


