package com.evoting.evoting.system.service.serviceForCandidate;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Candidates;
import com.evoting.evoting.system.dto.AdminRequest;
import com.evoting.evoting.system.dto.CandidateRequest;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.Response;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService{
    @Autowired
    private CandidatesRepository candidatesRepository;

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
        Candidates candidates = Candidates.builder()
                .firstName(candidateRequest.getFirstName())
                .middleName(candidateRequest.getMiddleName())
                .lastName(candidateRequest.getLastName())
                .email(candidateRequest.getEmail())
                .dateOfBirth(candidateRequest.getDateOfBirth())
                .email(candidateRequest.getEmail())
                .phoneNumber(candidateRequest.getPhoneNumber())
                .politicalParty(candidateRequest.getPoliticalParty())
                .slogan(candidateRequest.getSlogan())
                .campaignWebsite(candidateRequest.getCampaignWebsite())
                .socialMediaHandles(candidateRequest.getSocialMediaHandles())
                .Photo(candidateRequest.getPhoto())
                .candidateStatus("Active")
                .build();

        Candidates savedCandidate = candidatesRepository.save(candidates);
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
        List<Candidates> candidates = candidatesRepository.findAll();

        //response on all the customers and there details
        List<Response> responseList = new ArrayList<>();
        for (Candidates candidate : candidates) {
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
        Candidates candidates = Candidates.builder()
                .firstName(candidateRequest.getFirstName())
                .middleName(candidateRequest.getMiddleName())
                .lastName(candidateRequest.getLastName())
                .email(candidateRequest.getEmail())
                .dateOfBirth(candidateRequest.getDateOfBirth())
                .email(candidateRequest.getEmail())
                .phoneNumber(candidateRequest.getPhoneNumber())
                .politicalParty(candidateRequest.getPoliticalParty())
                .slogan(candidateRequest.getSlogan())
                .campaignWebsite(candidateRequest.getCampaignWebsite())
                .socialMediaHandles(candidateRequest.getSocialMediaHandles())
                .Photo(candidateRequest.getPhoto())
                .candidateStatus("Active")
                .build();
        Candidates savedCandidate = candidatesRepository.save(candidates);
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
        Optional<Candidates> candidate = candidatesRepository.findById(id);
        candidate.get().setDeleteStatus(true);
        Candidates saveCustomerInfo = candidatesRepository.save(candidate.get());

        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }
}
