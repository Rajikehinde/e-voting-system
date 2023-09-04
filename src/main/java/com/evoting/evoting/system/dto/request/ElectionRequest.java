package com.evoting.evoting.system.dto.request;

import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectionRequest {
    private Long electionId;
    private String electionName;
    private LocalDate electionDate;
    private LocalTime electionTimeStart;
    private LocalTime electionTimeOut;
}
