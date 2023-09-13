package com.evoting.evoting.system.email.emailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String messageBody;
    private String recipient;
    private String attachment;
    private String subject;
    private String cardNo;
}
