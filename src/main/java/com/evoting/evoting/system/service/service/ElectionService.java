package com.evoting.evoting.system.service.service;

import com.evoting.evoting.system.dto.request.ElectionRequest;
import com.evoting.evoting.system.dto.response.Response;

public interface ElectionService {
    Response voteStarting (ElectionRequest electionRequest);
}
