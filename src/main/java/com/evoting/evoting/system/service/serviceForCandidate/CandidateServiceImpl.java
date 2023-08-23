package com.evoting.evoting.system.service.serviceForCandidate;

import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.dto.request.CandidateRequest;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.email.emailDto.EmailDetails;
import com.evoting.evoting.system.email.emailService.EmailService;
import com.evoting.evoting.system.exception.AlreadyVotedException;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService{
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;
    private final CandidatesRepository candidatesRepository;

    public CandidateServiceImpl(CandidatesRepository candidatesRepository) {
        this.candidatesRepository = candidatesRepository;
    }

    @Override
    public Response registerCandidate(CandidateRequest candidateRequest) {
        Boolean isExists = candidatesRepository.existsByEmail(candidateRequest.getEmail());
        if (isExists){
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
//        Politics candidates = new Politics();
//        candidates.setFirstName(candidateRequest.getFirstName());
//        candidates.setLastName(candidateRequest.getLastName());
//        candidates.setMiddleName(candidateRequest.getMiddleName());
//        candidates.setEmail(candidateRequest.getEmail());
//        candidates.setDateOfBirth(candidateRequest.getDateOfBirth());
//        candidates.setPhoneNumber(candidateRequest.getPhoneNumber());
//        candidates.setPhoto(candidateRequest.getPhoto());
//        candidates.setBiography(candidateRequest.getBiography());
//        candidates.setSocialMediaHandles(candidateRequest.getSocialMediaHandles());
//        candidates.setCandidateType(candidateRequest.getCandidateType());
////        candidates.setPoliticalParty(candidateRequest.getPoliticalParty());
//        candidates.setCampaignWebsite(candidateRequest.getCampaignWebsite());
//        candidates.setSlogan(candidateRequest.getSlogan());
        Candidate candidate = Candidate.builder()
                .firstName(candidateRequest.getFirstName())
                .lastName(candidateRequest.getLastName())
                .middleName(candidateRequest.getMiddleName())
                .email(candidateRequest.getEmail())
                .dateOfBirth(candidateRequest.getDateOfBirth())
                .phoneNumber(candidateRequest.getPhoneNumber())
                .biography(candidateRequest.getBiography())
                .socialMediaHandles(candidateRequest.getSocialMediaHandles())
                .campaignWebsite(candidateRequest.getCampaignWebsite())
                .slogan(candidateRequest.getSlogan())
                .voteCategory(candidateRequest.getVoteCategory())
                .party(candidateRequest.getParty())
                .Photo(candidateRequest.getPhoto())
                .build();
        Candidate savedCandidate = candidatesRepository.save(candidate);
        try {

        }catch (Exception e){
            throw new AlreadyVotedException("Thanks for your participation. No slot for this category");
        }

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedCandidate.getEmail())
                .subject("Voting")
                .messageBody("Thank you for exercising your franchise.\n" +
                        "Voter's Name: " + savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);
        return Response.builder()
                .code(ResponseUtils.USER_REGISTER_CODE)
                .message(ResponseUtils.USER_REGISTER_MESSAGE)
                .data(Data.builder()
                        .name(savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                        .build())
                .build();
    }

    @Override
    public List<Response> fetchAllCandidates() {
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
        Boolean isExists = candidatesRepository.existsByEmail(candidateRequest.getEmail());
        if (!isExists){
            return Response.builder()
                    .code(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
//        Politics candidate = new Politics();
//        candidatesRepository.findByEmail(candidateRequest.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//                candidate.setFirstName(candidateRequest.getFirstName());
//                candidate.setMiddleName(candidateRequest.getMiddleName());
//                candidate.setLastName(candidateRequest.getLastName());
//                candidate.setEmail(candidateRequest.getEmail());
//                candidate.setDateOfBirth(candidateRequest.getDateOfBirth());
//                candidate.setPhoneNumber(candidateRequest.getPhoneNumber());
////                candidate.setPoliticalParty(candidateRequest.getPoliticalParty());
//                candidate.setSlogan(candidateRequest.getSlogan());
//                candidate.setCampaignWebsite(candidateRequest.getCampaignWebsite());
//                candidate.setSocialMediaHandles(candidateRequest.getSocialMediaHandles());
//                candidate.setPhoto(candidateRequest.getPhoto());
//        Candidate savedCandidate = candidatesRepository.save(candidate);
        Candidate candidate = Candidate.builder()
                .firstName(candidateRequest.getFirstName())
                .lastName(candidateRequest.getLastName())
                .middleName(candidateRequest.getMiddleName())
                .email(candidateRequest.getEmail())
                .dateOfBirth(candidateRequest.getDateOfBirth())
                .phoneNumber(candidateRequest.getPhoneNumber())
                .biography(candidateRequest.getBiography())
                .socialMediaHandles(candidateRequest.getSocialMediaHandles())
                .campaignWebsite(candidateRequest.getCampaignWebsite())
                .slogan(candidateRequest.getSlogan())
                .voteCategory(candidateRequest.getVoteCategory())
                .Photo(candidateRequest.getPhoto())
                .build();
                Candidate savedCandidate = candidatesRepository.save(candidate);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedCandidate.getEmail())
                .subject("Voting")
                .messageBody("Thank you for exercising your franchise.\n" +
                        "Voter's Name: " + savedCandidate.getFirstName() + " " + savedCandidate.getMiddleName() + " " + savedCandidate.getLastName())
                .build();
        emailService.sendSimpleEmail(emailDetails);

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
        boolean isExist = candidatesRepository.existsById(id);
        if (!isExist){
            return Response.builder()
                    .message(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        Optional<Candidate> candidate = candidatesRepository.findById(id);
        candidate.get().setDeleteStatus(true);
        Candidate saveCustomerInfo = candidatesRepository.save(candidate.get());

        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }
}
