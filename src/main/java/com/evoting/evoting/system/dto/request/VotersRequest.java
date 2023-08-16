package com.evoting.evoting.system.dto.request;

import com.evoting.evoting.system.domain.enmPackage.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotersRequest {
    private String lastName;
    private String middleName;
    private String firstName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private String votingDistrictConstituency;
    private String address;
    private String username;
    private String password;
    private String biometricData;
//    private String languagePreference;
    private boolean hasVotedForGovernor;
    private boolean hasVotedForHouseOfRepMember;
    private boolean hasVotedForSenateMember;
    private boolean hasVotedForHouseOfAssemblyMember;
}
