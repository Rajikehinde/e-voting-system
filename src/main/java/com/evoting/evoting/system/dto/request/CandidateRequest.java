package com.evoting.evoting.system.dto.request;

import com.evoting.evoting.system.domain.VoteCount;
import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateRequest {
    private String lastName;
    private String middleName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String biography;
    private String email;
    private String phoneNumber;
    private VoteCategory voteCategory;
    private String slogan;
    private String username;
    private String password;
    private VoteCount voteCount;
    private String campaignWebsite;
    private String socialMediaHandles;
    private String Photo;
    private Party party;
    private boolean hasVotedForGovernor;
    private boolean hasVotedForPresident;
    private boolean hasVotedForHouseOfRepMember;
    private boolean hasVotedForSenateMember;
    private boolean hasVotedForHouseOfAssemblyMember;
}
