package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Candidates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;
    private String lastName;
    private String middleName;
    private String firstName;
    private Date dateOfBirth;
    private String biography;
    private String email;
    private String phoneNumber;
    private String politicalParty;
    private String slogan;
    private String campaignWebsite;
    private String socialMediaHandles;
    private String Photo;
    private Boolean deleteStatus;
    private String candidateStatus;
}
