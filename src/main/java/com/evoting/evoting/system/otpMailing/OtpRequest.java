package com.evoting.evoting.system.otpMailing;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Getter
public class OtpRequest {
        private String phoneNumber;
        private String pin;
}
