package com.evoting.evoting.system.utils;

import com.evoting.evoting.system.domain.Candidate;
import com.evoting.evoting.system.domain.Voter;
import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.repository.CandidatesRepository;
import com.evoting.evoting.system.repository.VotersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Utils {

    private VotersRepository votersRepository;
    private CandidatesRepository candidatesRepository;


}
