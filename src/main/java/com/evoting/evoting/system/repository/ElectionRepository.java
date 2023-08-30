package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    Optional<Election> findById(Long electionId);
}