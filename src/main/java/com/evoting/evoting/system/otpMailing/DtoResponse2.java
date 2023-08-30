package com.evoting.evoting.system.otpMailing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DtoResponse2 {
    private String pinId;
    private String verified;
    private String msisdn;
}
