package com.icms.claimservice.request;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClaimRequest {

    @Positive(message = "Claim amount must be greater than 0.")
    private Double claimAmount;

    @PastOrPresent(message = "Claim date cannot be in the future.")
    private LocalDate claimDate;
}
