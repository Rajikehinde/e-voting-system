package com.evoting.evoting.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotersRequest {
    private String lastName;
    private String middleName;
    private String firstName;
    @CreationTimestamp
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private boolean voterStatus;
    private String votingDistrictConstituency;
    private String address;
    private String username;
    private String password;
    private String biometricData;
    private String languagePreference;
}
