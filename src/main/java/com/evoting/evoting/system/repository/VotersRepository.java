package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotersRepository extends JpaRepository<Voter,Long> {
     Boolean existsByEmail(String email);
    Optional<Voter> findByUsername(String username);
    Voter findByEmail(String email);
    Optional<Voter> findById(Long voterId);
}
