package com.icms.claimservice.service;

import com.icms.claimservice.event.policy.PolicyActivatedEvent;
import com.icms.claimservice.event.policy.PolicyCreatedEvent;
import com.icms.claimservice.event.policy.PolicyDeactivatedEvent;
import com.icms.claimservice.event.policy.PolicyUpdatedEvent;
import com.icms.claimservice.exception.ResourceNotFoundException;

public interface PolicyCacheService {

    void addPolicyCache(PolicyCreatedEvent policyCreatedEvent);

    void activatePolicyCache(PolicyActivatedEvent policyActivatedEvent) throws ResourceNotFoundException;

    void deactivatePolicyCache(PolicyDeactivatedEvent policyDeactivatedEvent) throws ResourceNotFoundException;

    void updatePolicyCache(PolicyUpdatedEvent policyUpdatedEvent) throws ResourceNotFoundException;
}
