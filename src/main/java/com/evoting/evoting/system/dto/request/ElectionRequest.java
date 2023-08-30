package com.evoting.evoting.system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectionRequest {
    private Long electionId;
    private String electionName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
