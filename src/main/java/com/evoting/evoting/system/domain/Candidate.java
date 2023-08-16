package com.evoting.evoting.system.domain;

import com.evoting.evoting.system.domain.enmPackage.Party;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@MappedSuperclass
//@EntityListeners(Politics.class)
@AllArgsConstructor
@NoArgsConstructor
//@Builder
@Getter
@Setter
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
    private String socialMediaHandles;
    private Boolean deleteStatus;
    private String Photo;
    @Enumerated(value = EnumType.STRING)
    private VoteCategory candidateType;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "Candidate_role", joinColumns = @JoinColumn(name = "Candidate_id",referencedColumnName = "candidateId"),
            inverseJoinColumns = @JoinColumn(name = "election_id",referencedColumnName = "electionId"))
    private List<Election> election;
}
