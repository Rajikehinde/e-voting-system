package com.evoting.evoting.system.otpMailing;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OtpEntity implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String pinId;
        private String destinationNumber;
        private String smsStatus;
        private String verificationStatus;
}
