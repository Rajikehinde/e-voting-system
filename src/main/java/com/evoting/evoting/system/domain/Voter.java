package com.evoting.evoting.system.domain;

import com.evoting.evoting.system.domain.enmPackage.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voterId;
    private String lastName;
    private String middleName;
    private String firstName;
    private Gender gender;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private Boolean voterStatus;
    private String state;
    private String localGovernment;
    private String registrationNo;
    private String bvn;
//    private String pollingUnit;
//    private String pollingUnitCode;
//    private String votingDistrictConstituency;
    private String address;
    private String username;
    private String password;
    private String biometricData;
    private Boolean deleteStatus;
    private boolean hasVotedForGovernor;
    private boolean hasVotedForHouseOfRepMember;
    private boolean hasVotedForSenateMember;
    private boolean hasVotedForHouseOfAssemblyMember;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "Voter_role", joinColumns = @JoinColumn(name = "Voter_id",referencedColumnName = "voterId"),
            inverseJoinColumns = @JoinColumn(name = "election_id",referencedColumnName = "electionId"))
    private List<Election> election;

    @OneToOne
    @JoinColumn(name = "voteCount_id")
    private VoteCount voteCount;
}
