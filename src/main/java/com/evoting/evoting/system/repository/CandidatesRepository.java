package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Politics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatesRepository extends JpaRepository<Politics,Long> {
    Boolean existsByEmail (String email);
    Optional<Candidate> findByEmail(String email);
}
