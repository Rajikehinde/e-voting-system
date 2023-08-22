package com.evoting.evoting.system.service.serviceForVoteCount;

import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.VoteCount;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.dto.request.ViewResultResponse;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.exception.AlreadyVotedException;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.VoteCountRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import com.evoting.evoting.system.service.serviceForAuth.AuthServiceImpl;
import com.evoting.evoting.system.utils.ResponseUtils;
import com.evoting.evoting.system.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service

public class VoteCountServiceImpl implements VoteCountService{
    @Autowired
    private VotersRepository votersRepository;
    @Autowired
    private VoteCountRepository voteCountRepository;
    @Autowired
    private CandidatesRepository candidatesRepository;
    @Autowired
   private AuthServiceImpl authService;
    @Autowired
    private Utils utils;

    @Override
    public Response castVoteForPresidency(CastVoteRequest castVoteRequest) {
        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
        try {
            if (voter.isHasVotedForPresident()){
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
        }catch (Exception e){
            throw new AlreadyVotedException("You have already cast your vote for your preferred candidate");
        }
        votersRepository.save(voter);

//        if (!(castVoteRequest.getVoteCategory()).equals(VoteCategory.PRESIDENCY)){
//            throw new RuntimeException("Invalid vote category");
//        }
//        Optional<Candidate> optionalCandidate = Optional.ofNullable(utils.checkCandidateValidity(castVoteRequest).orElseThrow(() -> new UsernameNotFoundException("Candidate not existed")));
       Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.PRESIDENCY,castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()){
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        candidate.setVoteCategory(VoteCategory.PRESIDENCY);
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        voter.setHasVotedForPresident(true);

        candidatesRepository.save(candidate);
        VoteCount voteCount = VoteCount.builder()
                .candidate(candidate)
                .voter(voter)
                .voterCount(1L)
                .build();
        voteCountRepository.save(voteCount);
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    @Override
    public Response castVoteForGovernor(CastVoteRequest castVoteRequest) {
        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
        try {
            if (voter.isHasVotedForGovernor()){
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
        }catch (Exception e){
            throw new AlreadyVotedException("You have already cast your vote for your preferred candidate");
        }
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.GOVERNOR,castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()){
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        candidate.setVoteCategory(VoteCategory.GOVERNOR);
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        voter.setHasVotedForGovernor(true);

        votersRepository.save(voter);
        candidatesRepository.save(candidate);
        VoteCount voteCount = VoteCount.builder()
                .candidate(candidate)
                .voter(voter)
                .voterCount(1L)
                .build();
        voteCountRepository.save(voteCount);
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    @Override
    public Response castVoteForSENATE(CastVoteRequest castVoteRequest) {
        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
        try {
            if (voter.isHasVotedForSenateMember()){
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
        }catch (Exception e){
            throw new AlreadyVotedException("You have already cast your vote for your preferred candidate");
        }
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.SENATE,castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()){
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        candidate.setVoteCategory(VoteCategory.SENATE);
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        voter.setHasVotedForSenateMember(true);

        votersRepository.save(voter);
        candidatesRepository.save(candidate);
        VoteCount voteCount = VoteCount.builder()
                .candidate(candidate)
                .voter(voter)
                .voterCount(1L)
                .build();
        voteCountRepository.save(voteCount);
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    @Override
    public Response castVoteForHOUSE_OF_ASSEMBLY(CastVoteRequest castVoteRequest) {
        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
        try {
            if (voter.isHasVotedForHouseOfAssemblyMember()){
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
        }catch (Exception e){
            throw new AlreadyVotedException("You have already cast your vote for your preferred candidate");
        }
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.HOUSE_OF_ASSEMBLY,castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()){
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        candidate.setVoteCategory(VoteCategory.HOUSE_OF_ASSEMBLY);
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        voter.setHasVotedForHouseOfAssemblyMember(true);

        votersRepository.save(voter);
        candidatesRepository.save(candidate);
        VoteCount voteCount = VoteCount.builder()
                .candidate(candidate)
                .voter(voter)
                .voterCount(1L)
                .build();
        voteCountRepository.save(voteCount);
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    @Override
    public Response castVoteForHOUSE_OF_REPRESENTATIVE(CastVoteRequest castVoteRequest) {
        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
        try {
            if (voter.isHasVotedForHouseOfRepMember()){
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
        }catch (Exception e){
            throw new AlreadyVotedException("You have already cast your vote for your preferred candidate");
        }

        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.HOUSE_OF_REPRESENTATIVE,castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()){
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        candidate.setVoteCategory(VoteCategory.HOUSE_OF_REPRESENTATIVE);
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        voter.setHasVotedForHouseOfRepMember(true);

        votersRepository.save(voter);
        candidatesRepository.save(candidate);
        VoteCount voteCount = VoteCount.builder()
                .candidate(candidate)
                .voter(voter)
                .voterCount(1L)
                .build();
        voteCountRepository.save(voteCount);
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

//    @Override
//    public Response castVoteForGovernor(CastVoteRequest castVoteRequest) {
//        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
//        if (voter.isHasVotedForGovernor()){
//            throw new IllegalArgumentException("You have already cast your vote for your preferred candidate");
//        }
//        if (!(castVoteRequest.getVoteCategory()).equals(VoteCategory.GOVERNOR)){
//            throw new RuntimeException("Invalid vote category");
//        }
//        Optional<Candidate> candidate = Optional.ofNullable(utils.checkCandidateValidity(castVoteRequest).orElseThrow(() -> new UsernameNotFoundException("Candidate not existed")));
//        candidate.get().setVoteCount(candidate.get().getVoteCount()+ 1);
//        voter.setHasVotedForPresident(true);
//        candidatesRepository.save(candidate.get());
//        return Response.builder()
//                .code(ResponseUtils.VOTE_CASTED_CODE)
//                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
//                .build();
//    }
//
//    @Override
//    public Response castVoteForSENATE(CastVoteRequest castVoteRequest) {
//        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
//        if (voter.isHasVotedForSenateMember()){
//            throw new IllegalArgumentException("You have already cast your vote for your preferred candidate");
//        }
//        if (!(castVoteRequest.getVoteCategory()).equals(VoteCategory.SENATE)){
//            throw new RuntimeException("Invalid vote category");
//        }
//        Optional<Candidate> candidate = Optional.ofNullable(utils.checkCandidateValidity(castVoteRequest).orElseThrow(() -> new UsernameNotFoundException("Candidate not existed")));
//        candidate.get().setVoteCount(candidate.get().getVoteCount()+ 1);
//        voter.setHasVotedForSenateMember(true);
//        candidatesRepository.save(candidate.get());
//        return Response.builder()
//                .code(ResponseUtils.VOTE_CASTED_CODE)
//                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
//                .build();
//    }
//
//    @Override
//    public Response castVoteForHOUSE_OF_ASSEMBLY(CastVoteRequest castVoteRequest) {
//        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
//        if (voter.isHasVotedForHouseOfAssemblyMember()){
//            throw new IllegalArgumentException("You have already cast your vote for your preferred candidate");
//        }
//        if (!(castVoteRequest.getVoteCategory()).equals(VoteCategory.HOUSE_OF_ASSEMBLY)){
//            throw new RuntimeException("Invalid vote category");
//        }
//        Optional<Candidate> candidate = Optional.ofNullable(utils.checkCandidateValidity(castVoteRequest).orElseThrow(() -> new UsernameNotFoundException("Candidate not existed")));
//        candidate.get().setVoteCount(candidate.get().getVoteCount()+ 1);
//        voter.setHasVotedForHouseOfAssemblyMember(true);
//        candidatesRepository.save(candidate.get());
//        return Response.builder()
//                .code(ResponseUtils.VOTE_CASTED_CODE)
//                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
//                .build();
//    }
//
//    @Override
//    public Response castVoteForHOUSE_OF_REPRESENTATIVE(CastVoteRequest castVoteRequest) {
//        Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
//        if (voter.isHasVotedForHouseOfRepMember()){
//            throw new IllegalArgumentException("You have already cast your vote for your preferred candidate");
//        }
//        if (!(castVoteRequest.getVoteCategory()).equals(VoteCategory.HOUSE_OF_REPRESENTATIVE)){
//            throw new RuntimeException("Invalid vote category");
//        }
//        Optional<Candidate> candidate = Optional.ofNullable(utils.checkCandidateValidity(castVoteRequest).orElseThrow(() -> new UsernameNotFoundException("Candidate not existed")));
//        candidate.get().setVoteCount(candidate.get().getVoteCount()+ 1);
//        voter.setHasVotedForHouseOfRepMember(true);
//        candidatesRepository.save(candidate.get());
//        return Response.builder()
//                .code(ResponseUtils.VOTE_CASTED_CODE)
//                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
//                .build();
//    }

    @Override
    public ViewResultResponse viewPresidentialResultInPercentage() {
        List<Candidate> presidentialCandidates = candidatesRepository.findByVoteCategory(VoteCategory.PRESIDENCY);
        return getResultFor(presidentialCandidates);
    }

    @Override
    public ViewResultResponse viewGovernorshipResultInPercentage() {
        List<Candidate> governorshipCandidates = candidatesRepository.findByVoteCategory(VoteCategory.GOVERNOR);
        return getResultFor(governorshipCandidates);
    }

    @Override
    public ViewResultResponse viewHouseOfRepresentativeResultInPercentage() {
        List<Candidate> houseOfRepresentativeCandidates = candidatesRepository.findByVoteCategory(VoteCategory.HOUSE_OF_REPRESENTATIVE);
        return getResultFor(houseOfRepresentativeCandidates);
    }

    @Override
    public ViewResultResponse viewSenateResultInPercentage() {
        List<Candidate> senatorialCandidates = candidatesRepository.findByVoteCategory(VoteCategory.SENATE);
        return getResultFor(senatorialCandidates);
    }

    @Override
    public ViewResultResponse viewHouseOfAssemblyResultInPercentage() {
        List<Candidate> houseOfAssemblyCandidates = candidatesRepository.findByVoteCategory(VoteCategory.HOUSE_OF_ASSEMBLY);
        return getResultFor(houseOfAssemblyCandidates);
    }


    private ViewResultResponse getResultFor(List<Candidate> candidates) {
        long total = candidates.stream().mapToLong(Candidate::getVoteCount).count();
        Map<String, String> result = new HashMap<>();
        for(Candidate candidate : candidates){
            result.put(candidate.getParty().toString(), String.valueOf(candidate.getVoteCount()/total *100));
        }
        return new ViewResultResponse(result);
    }
}
