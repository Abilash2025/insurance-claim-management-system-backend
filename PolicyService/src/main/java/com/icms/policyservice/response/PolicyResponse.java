package com.icms.policyservice.response;

import com.icms.policyservice.enums.PolicyStatus;
import com.icms.policyservice.enums.PolicyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponse {

    private Integer policyNumber;

    private PolicyType policyType;

    private Double premium;

    private Double coverage;

    private Integer customerId;

    private PolicyStatus status;
}
