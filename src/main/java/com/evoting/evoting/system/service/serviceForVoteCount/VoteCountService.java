package com.evoting.evoting.system.service.serviceForVoteCount;

import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.dto.request.ViewResultResponse;
import com.evoting.evoting.system.dto.response.Response;

public interface VoteCountService {
    Response castVoteForPresidency(CastVoteRequest castVoteRequest);
    Response castVoteForGovernor(CastVoteRequest castVoteRequest);
    Response castVoteForSENATE(CastVoteRequest castVoteRequest);
    Response castVoteForHOUSE_OF_ASSEMBLY(CastVoteRequest castVoteRequest);
    Response castVoteForHOUSE_OF_REPRESENTATIVE(CastVoteRequest castVoteRequest);
    ViewResultResponse viewPresidentialResultInPercentage();
    ViewResultResponse viewGovernorshipResultInPercentage();
    ViewResultResponse viewHouseOfRepresentativeResultInPercentage();
    ViewResultResponse viewSenateResultInPercentage();
    ViewResultResponse viewHouseOfAssemblyResultInPercentage();


}
