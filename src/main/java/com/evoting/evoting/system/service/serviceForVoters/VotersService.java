package com.evoting.evoting.system.service.serviceForVoters;

import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.dto.request.VotersRequest;

import java.util.List;

public interface VotersService {
    Response registerVoters (VotersRequest votersRequest);
    List<Response> fetchAllVoters();
    Response updateVoters(VotersRequest votersRequest);
    Response delete(Long id);


}
