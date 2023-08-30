package com.evoting.evoting.system.otpMailing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class DtoResponse {
    private String pinId;
    private String destinationNumber;
    private String smsStatus;
}
