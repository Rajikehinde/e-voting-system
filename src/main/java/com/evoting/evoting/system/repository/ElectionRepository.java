package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {
}
