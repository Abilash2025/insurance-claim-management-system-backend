package com.icms.policyservice.event.customer;

import com.icms.policyservice.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreatedEvent {

    private Integer customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
    private CustomerStatus status;
}
