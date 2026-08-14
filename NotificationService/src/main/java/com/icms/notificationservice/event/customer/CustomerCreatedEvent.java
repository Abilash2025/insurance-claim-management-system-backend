package com.icms.notificationservice.event.customer;


import com.icms.notificationservice.enums.CustomerStatus;
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
