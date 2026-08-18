package com.icms.policyservice.event.policy;

import com.icms.policyservice.enums.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyActivatedEvent {
    private Integer policyNumber;
    private PolicyStatus status;

}
