package com.icms.policyservice.entity;

import com.icms.policyservice.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_cache")
public class CustomerCacheEntity {

    @Id
    private Integer customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
}
