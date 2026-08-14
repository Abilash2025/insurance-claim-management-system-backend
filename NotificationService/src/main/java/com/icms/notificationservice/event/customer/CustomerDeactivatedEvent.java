package com.icms.notificationservice.event.customer;

import com.icms.notificationservice.enums.CustomerStatus;
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
