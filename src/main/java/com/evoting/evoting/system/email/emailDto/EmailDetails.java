package com.evoting.evoting.system.email.emailDto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String subject;
    private String messageBody;
    private String recipient;
    private String attachment;
    private String cardNo;
}
