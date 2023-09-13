package com.evoting.evoting.system.service.serviceForCandidate;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Role;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.CandidateRequest;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.email.emailDto.EmailDetails;
import com.evoting.evoting.system.email.emailService.EmailService;
//import com.evoting.evoting.system.exception.AlreadyVotedException;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.RoleRepository;
import com.evoting.evoting.system.service.serviceForElection.ElectionService;
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

@Service
@Slf4j
public class CandidateServiceImpl implements CandidateService{
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final CandidatesRepository candidatesRepository;

    public CandidateServiceImpl(CandidatesRepository candidatesRepository) {
        this.candidatesRepository = candidatesRepository;
    }

    @Override
    public Response registerCandidate(CandidateRequest candidateRequest) {
        //check if the candidate exists by email
        Boolean isExists = candidatesRepository.existsByEmail(candidateRequest.getEmail());
        if (isExists) {
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        //checking if candidate with a specific category of a party exists
        if (!candidatesRepository.existsByVoteCategoryAndParty(candidateRequest.getVoteCategory(), candidateRequest.getParty())) {
            //creating a candidate profile if not exist
            Candidate candidate = Candidate.builder()
                    .firstName(candidateRequest.getFirstName())
                    .lastName(candidateRequest.getLastName())
                    .middleName(candidateRequest.getMiddleName())
                    .email(candidateRequest.getEmail())
                    .dateOfBirth(candidateRequest.getDateOfBirth())
                    .phoneNumber(candidateRequest.getPhoneNumber())
                    .username(candidateRequest.getUsername())
                    .password(passwordEncoder.encode(candidateRequest.getPassword()))
                    .biography(candidateRequest.getBiography())
                    .socialMediaHandles(candidateRequest.getSocialMediaHandles())
                    .campaignWebsite(candidateRequest.getCampaignWebsite())
                    .slogan(candidateRequest.getSlogan())
                    .voteCategory(candidateRequest.getVoteCategory())
                    .party(candidateRequest.getParty())
                    .Photo(candidateRequest.getPhoto())
                    .build();

            //appending a role of security to the admin
            Role role = roleRepository.findByRoleName("ROLE_CANDIDATE").get();
            log.info("give me the role" + role);
            candidate.setRole(Collections.singleton(role));
            //saving the candidate in the database
            Candidate savedCandidate = candidatesRepository.save(candidate);

            //appending email to the candidate
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(savedCandidate.getEmail())
                    .subject("Candidate")
                    .messageBody("Candidate profile created.\n" +
                            "Candidate Name: " + savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetails);

            //returning a response to the created candidate
            return Response.builder()
                    .code(ResponseUtils.USER_REGISTER_CODE)
                    .message(ResponseUtils.USER_REGISTER_MESSAGE)
                    .data(Data.builder()
                            .name(savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                            .build())
                    .build();
        } else {
            return Response.builder().message("Thank you for your participation, this category is taken").build();
        }
    }
    @Override
    public List<Response> fetchAllCandidates() {
        //finding the candidates in the database if it exists
        List<Candidate> candidates = candidatesRepository.findAll();

        //response on all the customers and there details
        List<Response> responseList = new ArrayList<>();
        for (Candidate candidate : candidates) {
            responseList.add(Response.builder()
                    .code(ResponseUtils.USER_LIST_CODE)
                    .message(ResponseUtils.USER_LIST_MESSAGE)
                    .data(Data.builder()
                            .name(candidate.getFirstName() + " " + candidate.getMiddleName() + " " + candidate.getLastName())
                            .build())
                    .build());
        }
        return responseList;
    }

    @Override
    public Response updateCandidate(CandidateRequest candidateRequest) {
        //checking if candidate exists in the database by emails
        Boolean isExists = candidatesRepository.existsByEmail(candidateRequest.getEmail());
        if (!isExists){
            return Response.builder()
                    .code(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        //finding the existed admin in the database and updating it
        Candidate candidate = candidatesRepository.findFirstByVoteCategoryAndParty(candidateRequest.getVoteCategory(), candidateRequest.getParty())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        candidate.setLastName(candidateRequest.getLastName());
        candidate.setMiddleName(candidateRequest.getMiddleName());
        candidate.setFirstName(candidateRequest.getFirstName());
        candidate.setDateOfBirth(candidateRequest.getDateOfBirth());
        candidate.setEmail(candidateRequest.getEmail());
        candidate.setUsername(candidateRequest.getUsername());
        candidate.setPassword(passwordEncoder.encode(candidateRequest.getPassword()));
        candidate.setPhoneNumber(candidateRequest.getPhoneNumber());
        candidate.setCampaignWebsite(candidateRequest.getCampaignWebsite());
        candidate.setParty(candidateRequest.getParty());
        candidate.setVoteCategory(candidateRequest.getVoteCategory());
        candidate.setSocialMediaHandles(candidateRequest.getSocialMediaHandles());
        candidate.setPhoto(candidateRequest.getPhoto());

        //saving the update in the database
        Candidate savedCandidate = candidatesRepository.save(candidate);

                //appending email to the updated candidate
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedCandidate.getEmail())
                .subject("Candidate")
                .messageBody("Candidate profile updated.\n" +
                        "Candidate Name: " + savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);

        //returning response to the updated candidate
        return Response.builder()
                .code(ResponseUtils.USER_PROFILE_UPDATE_CODE)
                .message(ResponseUtils.USER_PROFILE_UPDATE_MESSAGE)
                .data(Data.builder()
                        .name(savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                        .build())
                .build();
    }

    @Override
    public Response delete(Long id) {
        //checking if candidate exists through id
        boolean isExist = candidatesRepository.existsById(id);
        if (!isExist){
            return Response.builder()
                    .message(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        //finding candidate by id
        Optional<Candidate> candidate = candidatesRepository.findById(id);
        candidate.get().setDeleteStatus(true);
        //saving deleted candidate in database
        Candidate saveCustomerInfo = candidatesRepository.save(candidate.get());

        //returning response of the deleted candidate
        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }
}
