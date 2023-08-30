package com.evoting.evoting.system.dto.request;

import com.evoting.evoting.system.domain.enmPackage.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotersRequest {
    private String lastName;
    private String middleName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private String gender;
    private String state;
    private String localGovernment;
//    private String registrationNo;
    private String address;
    private String username;
//    private String password;
//    private String languagePreference;
    private boolean hasVotedForGovernor;
    private boolean hasVotedForHouseOfRepMember;
    private boolean hasVotedForSenateMember;
    private boolean hasVotedForHouseOfAssemblyMember;
    private boolean hasVotedForPresident;
}
