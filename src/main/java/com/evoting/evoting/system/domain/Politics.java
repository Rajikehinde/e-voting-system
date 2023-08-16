package com.evoting.evoting.system.domain;

import com.evoting.evoting.system.domain.enmPackage.Party;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Politics extends Candidate{
    private Party party;
    private String slogan;
    private String campaignWebsite;
}
