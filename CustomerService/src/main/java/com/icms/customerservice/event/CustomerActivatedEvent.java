package com.icms.customerservice.event;

import com.icms.customerservice.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerActivatedEvent {

    private Integer customerId;
    private CustomerStatus customerStatus;

}
