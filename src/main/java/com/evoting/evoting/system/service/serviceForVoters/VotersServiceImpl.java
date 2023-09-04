package com.evoting.evoting.system.service.serviceForVoters;

import com.evoting.evoting.system.domain.Election;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.domain.enmPackage.Gender;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.dto.request.VotersRequest;
import com.evoting.evoting.system.email.emailDto.EmailDetails;
import com.evoting.evoting.system.email.emailService.EmailService;
import com.evoting.evoting.system.otpMailing.OtpEntity;
import com.evoting.evoting.system.otpMailing.OtpService;
import com.evoting.evoting.system.repository.ElectionRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import com.evoting.evoting.system.service.serviceForElection.ElectionServiceImpl;
import com.evoting.evoting.system.utils.ResponseUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class VotersServiceImpl implements VotersService{
    @Autowired
    EmailService emailService;
    @Autowired
    private VotersRepository votersRepository;
    @Autowired
    private ElectionRepository electionRepository;


    @Autowired
    private OtpService otpService;

    @Override
    public Response registerVoters(VotersRequest votersRequest) throws UnirestException {
        //checking if voter exists in the database through email
        Boolean isExist = votersRepository.existsByEmail(votersRequest.getEmail());
        if (isExist){
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        //creating a voter in the database
        Voter voters = Voter.builder()
                .firstName(votersRequest.getFirstName())
                .middleName(votersRequest.getMiddleName())
                .lastName(votersRequest.getLastName())
                .email(votersRequest.getEmail())
                .username(votersRequest.getUsername())
                .gender(Gender.valueOf(votersRequest.getGender()))
                .phoneNumber(votersRequest.getPhoneNumber())
                .dateOfBirth(votersRequest.getDateOfBirth())
                .address(votersRequest.getAddress())
                .state(votersRequest.getState())
                .localGovernment(votersRequest.getLocalGovernment())
                .build();

        //saving a voter in the database
        Voter savedVoters = votersRepository.save(voters);
        //implementation of Otp
//        otpService.sendOtpTrial(savedVoters.getPhoneNumber());

        //appending email response to the account
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedVoters.getEmail())
                .subject("Voter")
                .messageBody("Voter profile created.\n" +
                        "Voter Name: " + savedVoters.getFirstName() + " " + savedVoters.getMiddleName() + " " + savedVoters.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);

        //returning a response to the created voter
        return Response.builder()
                .code(ResponseUtils.USER_REGISTER_CODE)
                .message(ResponseUtils.USER_REGISTER_MESSAGE)
                .data(Data.builder()
                        .name(savedVoters.getFirstName() + " " + savedVoters.getMiddleName() + " " + savedVoters.getLastName())
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
        if (!isExists){
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
                .recipient(savedVotes.getEmail())
                .subject("Voter")
                .messageBody("Voter profile updated \n" +
                        "Voter Name: " + savedVotes.getFirstName() + " " + savedVotes.getMiddleName() + " " + savedVotes.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);
        //returning a response to updated voter
        return Response.builder()
                .code(ResponseUtils.USER_PROFILE_UPDATE_CODE)
                .message(ResponseUtils.USER_PROFILE_UPDATE_MESSAGE)
                .data(Data.builder()
                        .name(savedVotes.getFirstName() + " " + savedVotes.getMiddleName() + " " + savedVotes.getLastName())
                        .build())
                .build();
    }

    @Override
    public Response delete(Long id) {
        //checking if voter exists  by id
        boolean isExist = votersRepository.existsById(id);
        if (!isExist){
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

    @Override
    public boolean canVote(Long voterId, Long electionId) {
        Voter voter = votersRepository.findById(voterId).orElseThrow(()-> new UsernameNotFoundException("Not found"));
        Election election = electionRepository.findById(electionId).orElseThrow(()-> new UsernameNotFoundException("Not found"));

        if (voter != null && election != null){
            LocalDate date = LocalDate.now();
            //LocalDateTime time = LocalDateTime.now();

            // Check voter's date of birth
            if (voter.getDateOfBirth().plusYears(18).isAfter(date)){
                return false;
            }
        }
        return true;
    }

}
