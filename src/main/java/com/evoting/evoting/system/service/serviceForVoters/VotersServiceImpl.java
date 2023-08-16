package com.evoting.evoting.system.service.serviceForVoters;

import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.dto.Data;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.dto.request.VotersRequest;
import com.evoting.evoting.system.repository.VotersRepository;
import com.evoting.evoting.system.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class VotersServiceImpl implements VotersService{
    @Autowired
    private VotersRepository votersRepository;
    @Override
    public Response registerVoters(VotersRequest votersRequest) {
        Boolean isExist = votersRepository.existsByEmail(votersRequest.getEmail());
        if (isExist){
            return Response.builder()
                    .code(ResponseUtils.USER_EXIST_CODE)
                    .message(ResponseUtils.USER_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        Voter voters = Voter.builder()
                .firstName(votersRequest.getFirstName())
                .middleName(votersRequest.getMiddleName())
                .lastName(votersRequest.getLastName())
                .email(votersRequest.getEmail())
                .username(votersRequest.getUsername())
                .gender(votersRequest.getGender())
                .phoneNumber(votersRequest.getPhoneNumber())
                .dateOfBirth(votersRequest.getDateOfBirth())
                .build();

        Voter savedVoters = votersRepository.save(voters);
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
        Boolean isExists = votersRepository.existsByEmail(votersRequest.getEmail());
        if (!isExists){
            return Response.builder()
                    .code(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        Voter voter = votersRepository.findByUsername(votersRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        voter.setFirstName(votersRequest.getFirstName());
        voter.setMiddleName(votersRequest.getMiddleName());
        voter.setLastName(votersRequest.getLastName());
        voter.setEmail(votersRequest.getEmail());
        voter.setUsername(votersRequest.getUsername());
        voter.setGender(votersRequest.getGender());
        voter.setDateOfBirth(votersRequest.getDateOfBirth());
        voter.setPhoneNumber(votersRequest.getPhoneNumber());
        Voter savedVotes = votersRepository.save(voter);
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
        boolean isExist = votersRepository.existsById(id);
        if (!isExist){
            return Response.builder()
                    .message(ResponseUtils.USER_NOT_FOUND_CODE)
                    .message(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        Optional<Voter> voter = votersRepository.findById(id);
        voter.get().setDeleteStatus(true);
        Voter saveVoters = votersRepository.save(voter.get());

        return Response.builder()
                .code(ResponseUtils.USER_DELETE_CODE)
                .message(ResponseUtils.USER_DELETE_MESSAGE)
                .build();
    }

}
