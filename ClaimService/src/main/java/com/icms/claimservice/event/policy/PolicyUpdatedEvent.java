package com.icms.claimservice.event.policy;

import com.icms.claimservice.enums.PolicyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyUpdatedEvent {

    private Integer policyNumber;
    private PolicyType policyType;
    private Double premium;
    private Double coverage;

}
