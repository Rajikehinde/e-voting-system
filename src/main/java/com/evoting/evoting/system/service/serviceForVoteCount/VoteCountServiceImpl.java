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

    Election election = null;

    @Override
    public Response castVoteForPresidency(CastVoteRequest castVoteRequest) {
        //checking if voters and candidate exists in database
        boolean voterExists = votersRepository.existsByEmail(castVoteRequest.getEmail());
        boolean candidateExists = candidatesRepository.existsByEmail(castVoteRequest.getEmail());
        //finding candidate in the database and if not found throw a response not found so that it can be available to be voted for
        Optional<Candidate> candidate = Optional.ofNullable(candidatesRepository.findFirstByVoteCategoryAndParty(VoteCategory.PRESIDENCY, castVoteRequest.getParty()).orElseThrow(() -> new UsernameNotFoundException("Candidate not found")));
//        if (optionalCandidate.isEmpty()) {
//            Response.builder()
//                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_CODE)
//                    .code(ResponseUtils.CANDIDATE_NOT_EXISTED_MESSAGE)
//                    .build();
//        }
        //finding candidate if existing in database
//        Candidate candidate = optionalCandidate

        //if voter exists, then find it, and then all other conditions comes after.
        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            //check if found voter has already voted, if yes, throw already voted
            if (voter.isHasVotedForPresident()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
            //if voter hasn't voted, then find the category the available candidate is contesting for
            election = electionRepository.findByElectionName("PRESIDENCY");

            //after getting the category of the candidate, it is time to vote but before voting the time of the election has to be checked if it is
            //within time for the voter to vote for the candidate, if it is still on the voting can still be done
            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
                candidate.get().setVoteCategory(VoteCategory.PRESIDENCY);
                candidate.get().setVoteCount(candidate.get().getVoteCount() + 1);
                voter.setHasVotedForPresident(true);
                candidatesRepository.save(candidate.get());
                VoteCount voteCount = VoteCount.builder()
                        .candidate(candidate.get())
                        .voter(voter)
                        .voterCount(1L)
                        .build();
                //saving the vote count in the database
                voteCountRepository.save(voteCount);
                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                //sending email to the voter for voting
                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(voter.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetails);
                //if the time isn't withing the election timestamped then throw a response voting can't be done anymore
            } else {
                return Response.builder()
                        .code(ResponseUtils.ELECTION_CODE)
                        .message(ResponseUtils.ELECTION_MESSAGE)
                        .build();
            }
        }

        //checking if candidate exists also in other to be able to vote too for other candidates as a voter, then find it, and then all other conditions comes after.
        else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            //check if found voter has already voted, if yes, throw already voted
            if (can.isHasVotedForPresident()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }

            election = electionRepository.findByElectionName("PRESIDENCY");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
                candidate.get().setVoteCategory(VoteCategory.PRESIDENCY);
                candidate.get().setVoteCount(candidate.get().getVoteCount() + 1);
                can.setHasVotedForPresident(true);

                candidatesRepository.save(candidate.get());
                VoteCount voteCount = VoteCount.builder()
                        .candidate(candidate.get())
                        .voter(null)
                        .voterCount(1L)
                        .build();
                //saving the vote count in the database
                voteCountRepository.save(voteCount);
                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";

                EmailDetails emailDetail = EmailDetails.builder()
                        .recipient(can.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetail);
            }
            return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
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
//        Election election = null;
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
            election = electionRepository.findByElectionName("GOVERNOR");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
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

                String emailMessage = "Welcome to e-voting system \n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(voter.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetails);
            }else
                return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
        } else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForGovernor()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
            election = electionRepository.findByElectionName("GOVERNOR");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetail = EmailDetails.builder()
                        .recipient(can.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetail);
            }
            return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
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
//        Election election = null;

        if (voterExists) {
            Voter voter = votersRepository.findByEmail(castVoteRequest.getEmail());
            if (voter.isHasVotedForSenateMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Voter savedVotes = votersRepository.save(voter);
            election = electionRepository.findByElectionName("SENATE");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(voter.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetails);
            }else
                return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
        } else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForSenateMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }

            election = electionRepository.findByElectionName("SENATE");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
                candidate.setVoteCategory(VoteCategory.SENATE);
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetail = EmailDetails.builder()
                        .recipient(can.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetail);
            }
            return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
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
            election = electionRepository.findByElectionName("HOUSE_OF_ASSEMBLY");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(voter.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetails);
            }else
                return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
        } else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForHouseOfAssemblyMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }

            election = electionRepository.findByElectionName("HOUSE_OF_ASSEMBLY");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
                candidate.setVoteCategory(VoteCategory.HOUSE_OF_ASSEMBLY);
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetail = EmailDetails.builder()
                        .recipient(can.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetail);

            }
            return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();
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
            election = electionRepository.findByElectionName("HOUSE_OF_REPRESENTATIVE");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
                candidate.setVoteCategory(VoteCategory.HOUSE_OF_REPRESENTATIVE);
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(voter.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetails);
            }
            return Response.builder()
                    .code(ResponseUtils.VOTE_CASTED_CODE)
                    .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                    .build();
        }else if (candidateExists) {
            Candidate can = candidatesRepository.findByEmail(castVoteRequest.getEmail()).orElseThrow(()-> new RuntimeException("Candidate not found"));
            if (can.isHasVotedForHouseOfRepMember()) {
                return Response.builder()
                        .code(ResponseUtils.VOTE_ALREADY_CASTED_CODE)
                        .message(ResponseUtils.VOTE_ALREADY_CASTED_MESSAGE)
                        .build();
            }
//            Candidate savedVot = candidatesRepository.save(can);

            election = electionRepository.findByElectionName("HOUSE_OF_REPRESENTATIVE");

            if (confirmElectionTime(election.getElectionTimeStart(), election.getElectionTimeOut())) {
                candidate.setVoteCategory(VoteCategory.HOUSE_OF_REPRESENTATIVE);
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

                String emailMessage = "Welcome to e-voting system,\n\n"
                        + "Congratulations! Thank you for exercising your franchise \n\n"
                        + "if you have any questions, please send your email to kehinderaji28@gmail.com and we would be happy to answer them.\n\n"
                        + "Telephone: 08183086849\n"
                        + "electoral commission";
                EmailDetails emailDetail = EmailDetails.builder()
                        .recipient(can.getEmail())
                        .subject("Voting")
                        .messageBody(emailMessage)
                        .build();
                emailService.sendSimpleEmail(emailDetail);
            }
            return Response.builder()
                    .code(ResponseUtils.ELECTION_CODE)
                    .message(ResponseUtils.ELECTION_MESSAGE)
                    .build();        }

        return Response.builder()
                .code(ResponseUtils.VOTE_CASTED_CODE)
                .message(ResponseUtils.VOTE_CASTED_MESSAGE)
                .build();
    }

    //a method for getting the list of the results in percentages
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


    // the method that calculates the percentage of the results
    private ViewResultResponse getResultFor(List<Candidate> candidates) {
        long total = candidates.stream().mapToLong(Candidate::getVoteCount).count();
        Map<String, String> result = new HashMap<>();
        for (Candidate candidate : candidates) {
            result.put(candidate.getParty().toString(), String.valueOf(candidate.getVoteCount() *100 /total));
        }
        return new ViewResultResponse(result);
    }

    //method that shows how election time was done
    private boolean confirmElectionTime(LocalTime startTime, LocalTime endTime){
        return !LocalTime.now().isBefore(startTime) && !LocalTime.now().isAfter(endTime);
    }
}
