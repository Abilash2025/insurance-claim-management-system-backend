package com.icms.policyservice.event.policy;

import com.icms.policyservice.enums.PolicyStatus;
import com.icms.policyservice.enums.PolicyType;
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
