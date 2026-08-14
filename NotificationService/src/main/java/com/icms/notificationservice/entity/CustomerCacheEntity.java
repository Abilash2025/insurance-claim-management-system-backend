package com.icms.notificationservice.entity;

import com.icms.notificationservice.enums.CustomerStatus;
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

    @Column(nullable = false)
    @Id
    private Integer customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
}
