package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long electionId;
    private String electionName;
    private LocalDate electionDate;
    @Column(columnDefinition = "TIME")
    private LocalTime electionTimeStart;
    @Column(columnDefinition = "TIME")
    private LocalTime electionTimeOut;
    @OneToOne
    @JoinColumn(name = "voter_id")
    private Voter voter;

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL)
    private List<Candidate> candidates;
}
