package com.icms.notificationservice.event.policy;

import com.icms.notificationservice.enums.PolicyStatus;
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
