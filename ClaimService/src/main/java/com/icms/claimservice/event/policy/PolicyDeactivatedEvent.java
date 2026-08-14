package com.icms.claimservice.event.policy;

import com.icms.claimservice.enums.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDeactivatedEvent {

    private Integer policyNumber;
    private PolicyStatus status;
}
