package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.VoteCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCountRepository extends JpaRepository<VoteCount, Long> {
}
