package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VoteCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long voterId;
    private Long candidateId;
    private Long electionId;

//    @OneToOne
//    @JoinColumn(name = "voter_id")
//    private Voter voter;
}
