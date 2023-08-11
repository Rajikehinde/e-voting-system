package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.domain.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatesRepository extends JpaRepository<Candidates,Long> {
    Boolean existsByEmail (String email);
    Optional<Administration> findByEmail(String email);
}
