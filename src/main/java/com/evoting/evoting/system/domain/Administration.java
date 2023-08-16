package com.evoting.evoting.system.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "Admin_table")
public class Administration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    private String lastName;
    private String middleName;
    private String firstName;
    private String username; //TODO: to be refactored by using email rather than this username;
    private String password;
    private String email;
    private String phoneNumber;
    private Boolean deleteStatus;
    private LocalDate dateOfBirth;
    private boolean status;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "Administration_role", joinColumns = @JoinColumn(name = "Administration_id",referencedColumnName = "adminId"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private Set<Role> role;
}
