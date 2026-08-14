package com.icms.notificationservice.entity;

import com.icms.notificationservice.enums.PolicyStatus;
import com.icms.notificationservice.enums.PolicyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "policy_cache")
public class PolicyCacheEntity {

    @Column(nullable = false)
    @Id
    private Integer policyNumber;

    @Column(nullable = false)
    private Integer customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyType policyType;

    @Column(nullable = false)
    private Double coverage;

    @Column(nullable = false)
    private Double premium;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;
}
