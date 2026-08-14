package com.icms.customerservice.response;

import com.icms.customerservice.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Integer customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
    private CustomerStatus customerStatus;
}
