package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Administration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrationRepository extends JpaRepository<Administration, Long> {
    Boolean existsByEmail (String email);
    Optional<Administration> findByUsername(String username);
}
