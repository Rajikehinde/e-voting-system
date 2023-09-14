package com.evoting.evoting.system.dto.request;

import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CastVoteRequest {
    private Party party;
    private String email;
}
