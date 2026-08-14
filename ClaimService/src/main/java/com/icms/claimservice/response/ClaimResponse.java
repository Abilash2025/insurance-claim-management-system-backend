package com.icms.claimservice.response;

import com.icms.claimservice.enums.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimResponse {

    private Integer claimNumber;
    private Double claimAmount;
    private ClaimStatus claimStatus;
    private LocalDate claimDate;
    private Integer policyNumber;
}
