package com.evoting.evoting.system.email.emailService;

import com.evoting.evoting.system.email.emailDto.EmailDetails;

public interface EmailService {
    String sendSimpleEmail(EmailDetails emailDetails);
    String sendEmailWithAttachment(EmailDetails emailDetails);
}
