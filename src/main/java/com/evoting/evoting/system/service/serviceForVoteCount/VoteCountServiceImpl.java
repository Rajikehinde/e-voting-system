package com.evoting.evoting.system.service.serviceForVoteCount;

import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Election;
import com.evoting.evoting.system.domain.VoteCount;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.dto.request.ViewResultResponse;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.email.emailDto.EmailDetails;
import com.evoting.evoting.system.email.emailService.EmailService;
//import com.evoting.evoting.system.exception.AlreadyVotedException;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.ElectionRepository;
import com.evoting.evoting.system.repository.VoteCountRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import com.evoting.evoting.system.service.serviceForAuth.AuthServiceImpl;
import com.evoting.evoting.system.service.serviceForElection.ElectionServiceImpl;
import com.evoting.evoting.system.service.serviceForVoters.VotersServiceImpl;
import com.evoting.evoting.system.utils.ResponseUtils;
import com.evoting.evoting.system.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VoteCountServiceImpl implements VoteCountService {
    @Autowired
    EmailService emailService;
    @Autowired
    private VotersRepository votersRepository;
    @Autowired
    private VoteCountRepository voteCountRepository;
    @Autowired
    private VotersServiceImpl votersService;
    @Autowired
    private CandidatesRepository candidatesRepository;
    @Autowired
    private ElectionServiceImpl electionService;
    @Autowired
    private ElectionRepository electionRepository;
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private Utils utils;

    @Override
    public Response castVoteForPresidency(CastVoteRequest castVoteRequest) {
        boolean voterExists = votersRepository.existsByEmail(castVoteRequest.getEmail());
        boolean candidateExists = candidatesRepository.existsByEmail(castVoteRequest.getEmail());
        //finding voters and candidate in the database
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.PRESIDENCY, castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()) {
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        //finding candidate if existing in database
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            if (voter.isHasVotedForPresident()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
            Election election = electionRepository.findByElectionName("PRESIDENCY");
//            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())){
//            LocalTime startTime = Time.valueOf("")
            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())){
                candidate.setVoteCategory(VoteCategory.PRESIDENCY);
                candidate.setVoteCount(candidate.getVoteCount() + 1);
                voter.setHasVotedForPresident(true);
                candidatesRepository.save(candidate);
                VoteCount voteCount = VoteCount.builder()
                        .candidate(candidate)
                        .voter(voter)
                        .voterCount(1L)
                        .build();
                //saving the vote count in the database
                voteCountRepository.save(voteCount);

                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(voter.getEmail())
                        .subject("Voting")
                        .messageBody("Thank you for exercising your franchise.\n" +
                                "Voter's Name: " + voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName())
                        .build();
                emailService.sendSimpleEmail(emailDetails);
            }else{
                return Response.builder()
                        .code(ResponseUtils.ELECTION_CODE)
                        .message(ResponseUtils.ELECTION_MESSAGE)
                        .build();
            }
        }

        else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(()-> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForPresident()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Candidate savedVot = candidatesRepository.save(can);

            candidate.setVoteCategory(VoteCategory.PRESIDENCY);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            can.setHasVotedForPresident(true);

            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(null)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetail = EmailDetails.builder()
                    .recipient(can.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + can.getFirstName() + " " + can.getMiddleName() + " " + can.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetail);

        }
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }
    @Override
    public Response castVoteForGovernor(CastVoteRequest castVoteRequest) {
        boolean voterExists = votersRepository.existsByEmail(castVoteRequest.getEmail());
        boolean candidateExists = candidatesRepository.existsByEmail(castVoteRequest.getEmail());
        //finding voters and candidate in the database
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.GOVERNOR, castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()) {
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        //finding candidate if existing in database
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            if (voter.isHasVotedForGovernor()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Voter savedVotes = votersRepository.save(voter);

            candidate.setVoteCategory(VoteCategory.GOVERNOR);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            voter.setHasVotedForGovernor(true);
            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(voter)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(voter.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetails);

        } else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForGovernor()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }

            candidate.setVoteCategory(VoteCategory.GOVERNOR);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            can.setHasVotedForGovernor(true);

            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(null)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetail = EmailDetails.builder()
                    .recipient(can.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + can.getFirstName() + " " + can.getMiddleName() + " " + can.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetail);

        }
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    @Override
    public Response castVoteForSENATE(CastVoteRequest castVoteRequest) {
        boolean voterExists = votersRepository.existsByEmail(castVoteRequest.getEmail());
        boolean candidateExists = candidatesRepository.existsByEmail(castVoteRequest.getEmail());
        //finding voters and candidate in the database
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.SENATE, castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()) {
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        //finding candidate if existing in database
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            if (voter.isHasVotedForSenateMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Voter savedVotes = votersRepository.save(voter);

            candidate.setVoteCategory(VoteCategory.SENATE);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            voter.setHasVotedForSenateMember(true);
            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(voter)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(voter.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetails);

        } else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForSenateMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }

            candidate.setVoteCategory(VoteCategory.GOVERNOR);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            can.setHasVotedForSenateMember(true);

            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(null)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetail = EmailDetails.builder()
                    .recipient(can.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + can.getFirstName() + " " + can.getMiddleName() + " " + can.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetail);

        }
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    @Override
    public Response castVoteForHOUSE_OF_ASSEMBLY(CastVoteRequest castVoteRequest) {
        boolean voterExists = votersRepository.existsByEmail(castVoteRequest.getEmail());
        boolean candidateExists = candidatesRepository.existsByEmail(castVoteRequest.getEmail());
        //finding voters and candidate in the database
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.HOUSE_OF_ASSEMBLY, castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()) {
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        //finding candidate if existing in database
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            if (voter.isHasVotedForHouseOfAssemblyMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Voter savedVotes = votersRepository.save(voter);

            candidate.setVoteCategory(VoteCategory.HOUSE_OF_ASSEMBLY);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            voter.setHasVotedForHouseOfAssemblyMember(true);
            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(voter)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(voter.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetails);

        } else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForHouseOfAssemblyMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }

            candidate.setVoteCategory(VoteCategory.GOVERNOR);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            can.setHasVotedForHouseOfAssemblyMember(true);

            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(null)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetail = EmailDetails.builder()
                    .recipient(can.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + can.getFirstName() + " " + can.getMiddleName() + " " + can.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetail);

        }
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }
    @Override
    public Response castVoteForHOUSE_OF_REPRESENTATIVE(CastVoteRequest castVoteRequest) {
        boolean voterExists = votersRepository.existsByEmail(castVoteRequest.getEmail());
        boolean candidateExists = candidatesRepository.existsByEmail(castVoteRequest.getEmail());
        //finding voters and candidate in the database
        Optional<Candidate> optionalCandidate = candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.HOUSE_OF_REPRESENTATIVE, castVoteRequest.getParty());
        if (optionalCandidate.isEmpty()) {
            Response.builder()
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
                    .build();
        }
        //finding candidate if existing in database
        Candidate candidate = optionalCandidate.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            if (voter.isHasVotedForHouseOfRepMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Voter savedVotes = votersRepository.save(voter);

            candidate.setVoteCategory(VoteCategory.PRESIDENCY);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            voter.setHasVotedForHouseOfRepMember(true);
            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(voter)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(voter.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetails);

        }else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(()-> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForHouseOfRepMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Candidate savedVot = candidatesRepository.save(can);

            candidate.setVoteCategory(VoteCategory.PRESIDENCY);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            can.setHasVotedForHouseOfRepMember(true);

            candidatesRepository.save(candidate);
            VoteCount voteCount = VoteCount.builder()
                    .candidate(candidate)
                    .voter(null)
                    .voterCount(1L)
                    .build();
            //saving the vote count in the database
            voteCountRepository.save(voteCount);

            EmailDetails emailDetail = EmailDetails.builder()
                    .recipient(can.getEmail())
                    .subject("Voting")
                    .messageBody("Thank you for exercising your franchise.\n" +
                            "Voter's Name: " + can.getFirstName() + " " + can.getMiddleName() + " " + can.getLastName())
                    .build();
            emailService.sendSimpleEmail(emailDetail);

        }
        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

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
        for (Candidate candidate : candidates) {
            result.put(candidate.getParty().toString(), String.valueOf(candidate.getVoteCount() *100 /total));
        }
        return new ViewResultResponse(result);
    }

    private boolean confirmElectionTime(LocalTime startTime, LocalTime endTime){
        return !LocalTime.now().isBefore(startTime) && !LocalTime.now().isAfter(endTime);
    }
}
