package com.icms.notificationservice.service;

import com.icms.notificationservice.event.claim.ClaimApprovedEvent;
import com.icms.notificationservice.event.claim.ClaimCreatedEvent;
import com.icms.notificationservice.event.claim.ClaimRejectedEvent;
import com.icms.notificationservice.event.customer.CustomerActivatedEvent;
import com.icms.notificationservice.event.customer.CustomerCreatedEvent;
import com.icms.notificationservice.event.customer.CustomerDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyActivatedEvent;
import com.icms.notificationservice.event.policy.PolicyCreatedEvent;
import com.icms.notificationservice.event.policy.PolicyDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyUpdatedEvent;

public interface NotificationService {
    void notifyCustomerCreated(CustomerCreatedEvent event);

    void notifyCustomerActivated(CustomerActivatedEvent event);

    void notifyCustomerDeactivated(CustomerDeactivatedEvent event);

    void notifyPolicyCreated(PolicyCreatedEvent event);

    void notifyPolicyUpdated(PolicyUpdatedEvent event);

    void notifyPolicyActivated(PolicyActivatedEvent event);

    void notifyPolicyDeactivated(PolicyDeactivatedEvent event);

    void notifyClaimCreated(ClaimCreatedEvent event);

    void notifyClaimApproved(ClaimApprovedEvent event);

    void notifyClaimRejected(ClaimRejectedEvent event);
}
