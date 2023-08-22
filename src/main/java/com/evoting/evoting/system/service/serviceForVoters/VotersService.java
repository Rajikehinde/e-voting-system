package com.evoting.evoting.system.service.serviceForVoters;

import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.dto.request.VotersRequest;

import java.util.List;

public interface VotersService {
    Response registerVoters (VotersRequest votersRequest);
    Response castVoteForPresidency(CastVoteRequest castVoteRequest);
    Response castVoteForGovernorship(CastVoteRequest castVoteRequest);
    Response castVoteForHouseOfRepresentative(CastVoteRequest castVoteRequest);
    Response castVoteForSenate(CastVoteRequest castVoteRequest);
    Response castVoteForHouseOfAssembly(CastVoteRequest castVoteRequest);
    List<Response> fetchAllVoters();
    Response updateVoters(VotersRequest votersRequest);
    Response delete(Long id);
}
