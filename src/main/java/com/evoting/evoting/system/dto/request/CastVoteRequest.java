package com.evoting.evoting.system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CastVoteRequest {
    private String party;
    private String VoteCategory;
    private String email;
    private String password;
}
