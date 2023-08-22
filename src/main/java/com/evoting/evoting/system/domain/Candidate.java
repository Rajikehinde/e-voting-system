package com.evoting.evoting.system.domain;

import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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


    // TODO: 8/17/2023 This needs to change to many to one
//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JoinTable(name = "Candidate_role", joinColumns = @JoinColumn(name = "Candidate_id",referencedColumnName = "candidateId"),
//            inverseJoinColumns = @JoinColumn(name = "election_id",referencedColumnName = "electionId"))
//    private List<Election> election;
    @ManyToOne
    @JoinColumn(name = "electionId")
    private Election election;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<VoteCount> voteCounts;
}
