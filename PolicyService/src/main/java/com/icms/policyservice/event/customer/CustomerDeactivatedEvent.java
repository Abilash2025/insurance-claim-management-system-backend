package com.icms.policyservice.event.customer;

import com.icms.policyservice.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDeactivatedEvent {

    private Integer customerId;
    private CustomerStatus status;
}
