package com.evoting.evoting.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateRequest {
    private String lastName;
    private String middleName;
    private String firstName;
    @CreationTimestamp
    private Date dateOfBirth;
    private String biography;
    private String email;
    private String phoneNumber;
    private String politicalParty;
    private String slogan;
    private String campaignWebsite;
    private String socialMediaHandles;
    private String Photo;
    private String candidateStatus;
}
