package com.icms.claimservice.entity;

import com.icms.claimservice.enums.ClaimStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "claim")
public class ClaimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimNumber;

    @Column(nullable = false)
    private Double claimAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimStatus claimStatus;

    @Column(nullable = false)
    private Integer policyNumber;

    @Column(nullable = false)
    private LocalDate claimDate;
}
