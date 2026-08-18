package com.icms.policyservice.service;

import com.icms.policyservice.exception.CustomerInactiveException;
import com.icms.policyservice.exception.PolicyActiveException;
import com.icms.policyservice.exception.PolicyInactiveException;
import com.icms.policyservice.request.CreatePolicyRequest;
import com.icms.policyservice.exception.ResourceNotFoundException;
import com.icms.policyservice.request.UpdatePolicyRequest;
import com.icms.policyservice.response.PolicyResponse;

import java.util.List;

public interface PolicyService {

    PolicyResponse addPolicy(CreatePolicyRequest policy) throws ResourceNotFoundException, CustomerInactiveException;

    PolicyResponse getPolicyById(Integer id) throws ResourceNotFoundException;

    List<PolicyResponse> getAllPolicies();

    PolicyResponse updatePolicy(Integer id, UpdatePolicyRequest policy) throws ResourceNotFoundException, PolicyInactiveException;

    PolicyResponse deactivatePolicy(Integer id) throws ResourceNotFoundException, PolicyInactiveException;

    PolicyResponse activatePolicy(Integer id) throws ResourceNotFoundException, PolicyActiveException, CustomerInactiveException;

    void deactivatePoliciesByCustomer(Integer customerId);

    void activatePoliciesByCustomer(Integer customerId);
}
