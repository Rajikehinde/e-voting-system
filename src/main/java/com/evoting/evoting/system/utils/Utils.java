package com.evoting.evoting.system.utils;

import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Utils {

    private VotersRepository votersRepository;
    private CandidatesRepository candidatesRepository;
//    private static boolean partyIsValid(CastVoteRequest castVoteRequest) {
//        return EnumSet.allOf(Party.class)
//                .stream()
//                .anyMatch(party -> party.name().equals(castVoteRequest.getParty()));
//    }
//
//    public Optional<Candidate> checkCandidateValidity(CastVoteRequest castVoteRequest) {
//        Optional<Candidate> candidate = candidatesRepository.findByParty(castVoteRequest.getParty());
//        if(candidate.isEmpty()){
//            throw new IllegalArgumentException("No candidate found!");
//        }
//        if(!(candidate.get().getParty().equals(castVoteRequest.getParty()))) {
//            throw new IllegalArgumentException("Incorrect party name");
//        }
//        return candidate;
//    }
//    public Voter validateVoterCredentials(CastVoteRequest castVoteRequest) {
//        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
//        if(voter == null || !voter.getPassword().equals(castVoteRequest.getPassword())){
//            throw new IllegalArgumentException("Incorrect Password");
//        }
//        return voter;
//    }
}
