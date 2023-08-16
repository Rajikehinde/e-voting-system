package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Election {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long adminId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long electionId;
    private String electionName;
    private LocalDate electionDate;
    @OneToOne
    @JoinColumn(name = "voter_id")
    private Voter voter;
}
