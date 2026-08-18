package com.icms.policyservice.request;

import com.icms.policyservice.enums.PolicyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePolicyRequest {

    private Integer policyNumber;

    @NotNull(message = "Policy type is required.")
    private PolicyType policyType;

    @NotNull
    @Positive(message = "Premium must be greater than 0.")    private Double premium;

    @NotNull
    @Positive(message = "Coverage must be greater than 0.")
    private Double coverage;

    @NotNull(message = "Customer Id is required.")
    private Integer customerId;

}
