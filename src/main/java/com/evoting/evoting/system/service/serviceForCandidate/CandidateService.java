package com.evoting.evoting.system.service.serviceForCandidate;

import com.evoting.evoting.system.dto.request.CandidateRequest;
import com.evoting.evoting.system.dto.response.Response;

import java.util.List;

public interface CandidateService {
    Response registerCandidate (CandidateRequest candidateRequest);
    List<Response> fetchAllCandidates();
    Response updateCandidate(CandidateRequest candidateRequest);
    Response delete(Long id);
}
