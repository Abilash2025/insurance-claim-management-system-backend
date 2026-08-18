package com.icms.policyservice.entity;

import com.icms.policyservice.enums.PolicyStatus;
import com.icms.policyservice.enums.PolicyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "policy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer policyNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyType policyType;

    @Column(nullable = false)
    private Double premium;

    @Column(nullable = false)
    private Double coverage;

    @Column(nullable = false)
    private Integer customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;
}
