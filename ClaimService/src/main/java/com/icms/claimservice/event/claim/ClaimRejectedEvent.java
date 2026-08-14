package com.icms.claimservice.event.claim;

import com.icms.claimservice.enums.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimRejectedEvent {

    private Integer claimNumber;
    private Integer policyNumber;
    private ClaimStatus status;

}
