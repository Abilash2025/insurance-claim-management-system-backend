package com.icms.policyservice.request;

import com.icms.policyservice.enums.PolicyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePolicyRequest {

    private PolicyType policyType;

    @Positive(message = "Premium must be greater than 0.")
    private Double premium;

    @Positive(message = "Coverage must be greater than 0.")
    private Double coverage;

}
