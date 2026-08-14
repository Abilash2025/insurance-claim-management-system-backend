package com.icms.notificationservice.event.claim;

import com.icms.notificationservice.enums.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimCreatedEvent {

    private Integer claimNumber;
    private Integer policyNumber;
    private ClaimStatus status;
    private Double claimAmount;
    private LocalDate claimDate;
}
