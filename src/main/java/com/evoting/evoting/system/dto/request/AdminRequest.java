package com.evoting.evoting.system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AdminRequest {
    private String lastName;
    private String middleName;
    private String firstName;
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
}
