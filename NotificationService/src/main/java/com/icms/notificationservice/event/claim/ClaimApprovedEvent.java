package com.icms.notificationservice.event.claim;

import com.icms.notificationservice.enums.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimApprovedEvent {

    private Integer claimNumber;
    private Integer policyNumber;
    private ClaimStatus status;
}
