package com.evoting.evoting.system.domain;

import com.evoting.evoting.system.domain.enmPackage.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private String cardNo;
    private String state;
    private String localGovernment;
//    private String registrationNo;
    private String address;
    private String username;
    private String password;
    private Boolean deleteStatus;
    private boolean hasVotedForGovernor;
    private boolean hasVotedForPresident;
    private boolean hasVotedForHouseOfRepMember;
    private boolean hasVotedForSenateMember;
    private boolean hasVotedForHouseOfAssemblyMember;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "Voter_role", joinColumns = @JoinColumn(name = "Voter_id",referencedColumnName = "voterId"),
            inverseJoinColumns = @JoinColumn(name = "election_id",referencedColumnName = "electionId"))
    private List<Election> election;

    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL)
    private List<VoteCount> voteCounts;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "Voter_role", joinColumns = @JoinColumn(name = "Voter_id",referencedColumnName = "voterId"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private Set<Role> role;
}
