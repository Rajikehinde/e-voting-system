package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Candidate;

import com.evoting.evoting.system.domain.enmPackage.Party;
//import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatesRepository extends JpaRepository<Candidate,Long> {
    Boolean existsByEmail (String email);
    Optional<Candidate> findByUsername(String username);
    Boolean existsByUsername(String username);

    Optional<Candidate> findFirstByVoteCategoryAndParty (VoteCategory voteCategory,Party party);
    Boolean existsByVoteCategoryAndParty(VoteCategory voteCategory, Party party);
    Optional<Candidate> findByEmail (String email);
    List<Candidate> findByVoteCategory(VoteCategory voteCategory);
}
