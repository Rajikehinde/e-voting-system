package com.evoting.evoting.system.domain;

import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;
    private String lastName;
    private String middleName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String biography;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private long voteCount;
    private String socialMediaHandles;
    private Boolean deleteStatus;
    private String Photo;
    @Enumerated(EnumType.STRING)
    private Party party;
//    private VoteCategory candidateType;
    private String slogan;
    @Enumerated(EnumType.STRING)
    private VoteCategory voteCategory;
    private String campaignWebsite;
    private boolean hasVotedForGovernor;
    private boolean hasVotedForPresident;
    private boolean hasVotedForHouseOfRepMember;
    private boolean hasVotedForSenateMember;
    private boolean hasVotedForHouseOfAssemblyMember;

    @ManyToOne
    @JoinColumn(name = "electionId")
    private Election election;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<VoteCount> voteCounts;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "Candidate_role", joinColumns = @JoinColumn(name = "Candidate_id",referencedColumnName = "candidateId"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private Set<Role> role;
}
