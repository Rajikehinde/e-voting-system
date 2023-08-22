package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "voteCounts")
public class VoteCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteCount_id;
    private long voterCount;

    @ManyToOne
    @JoinColumn(name = "voterId")
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "candidateId")
    private Candidate candidate;
}
