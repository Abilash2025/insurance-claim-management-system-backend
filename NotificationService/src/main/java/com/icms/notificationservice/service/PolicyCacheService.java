package com.icms.notificationservice.service;

import com.icms.notificationservice.event.policy.PolicyActivatedEvent;
import com.icms.notificationservice.event.policy.PolicyCreatedEvent;
import com.icms.notificationservice.event.policy.PolicyDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyUpdatedEvent;

public interface PolicyCacheService {

    void addPolicyCache(PolicyCreatedEvent event);

    void updatePolicyCache(PolicyUpdatedEvent event);

    void activatePolicyCache(PolicyActivatedEvent event);

    void deactivatePolicyCache(PolicyDeactivatedEvent event);
}
