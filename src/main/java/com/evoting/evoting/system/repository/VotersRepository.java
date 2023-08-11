package com.evoting.evoting.system.repository;

import com.evoting.evoting.system.domain.Voters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotersRepository extends JpaRepository<Voters,Long> {
}
