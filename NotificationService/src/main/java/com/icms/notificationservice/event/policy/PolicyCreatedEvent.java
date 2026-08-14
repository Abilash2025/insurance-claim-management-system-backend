package com.icms.notificationservice.event.policy;

import com.icms.notificationservice.enums.PolicyStatus;
import com.icms.notificationservice.enums.PolicyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyCreatedEvent {

    private Integer policyNumber;
    private Integer customerId;
    private PolicyType policyType;
    private Double coverage;
    private Double premium;
    private PolicyStatus status;

}
