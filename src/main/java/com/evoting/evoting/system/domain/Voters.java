package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Voters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voterId;
    private String lastName;
    private String middleName;
    private String firstName;
    @CreationTimestamp
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private boolean voterStatus;
    private String state;
    private String localGovernment;
    @Column(name = "Registration/Area/Ward")
    private String registration;
    private String pollingUnit;
    private String pollingUnitCode;
    private String votingDistrictConstituency;
    private String address;
    private String username;
    private String password;
    private String biometricData;
    private String languagePreference;
}
