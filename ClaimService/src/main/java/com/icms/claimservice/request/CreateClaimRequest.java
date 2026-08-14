package com.icms.claimservice.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClaimRequest {

    @NotNull(message = "Claim amount cannot be null.")
    @Positive(message = "Claim amount must be greater than 0.")
    private Double claimAmount;

    @NotNull(message = "Policy number cannot be null.")
    private Integer policyNumber;

    @NotNull(message = "Claim date cannot be null.")
    @PastOrPresent(message = "Claim date cannot be in the future.")
    private LocalDate claimDate;
}
