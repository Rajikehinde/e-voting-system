package com.evoting.evoting.system.service.serviceForElection;

import com.evoting.evoting.system.dto.request.ElectionRequest;
import com.evoting.evoting.system.dto.response.Response;

public interface ElectionService {

    Void startingTime (ElectionRequest electionRequest);
    Void endingTime (ElectionRequest electionRequest);

}
