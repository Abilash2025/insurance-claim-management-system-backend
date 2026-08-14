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
import com.icms.notificationservice.eventpublisher.EmailEventPublisher;
import com.icms.notificationservice.exception.ResourceNotFoundException;
import com.icms.notificationservice.repository.CustomerCacheRepository;
import com.icms.notificationservice.repository.PolicyCacheRepository;

import com.icms.notificationservice.util.EmailTemplateBuilder;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final CustomerCacheRepository customerCacheRepository;
    private final PolicyCacheRepository policyCacheRepository;
    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailEventPublisher emailEventPublisher;

    public NotificationServiceImpl(CustomerCacheRepository customerCacheRepository,
                                   PolicyCacheRepository policyCacheRepository, EmailTemplateBuilder emailTemplateBuilder,
                                   EmailEventPublisher emailEventPublisher) {
        this.customerCacheRepository = customerCacheRepository;
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailEventPublisher = emailEventPublisher;
        this.policyCacheRepository = policyCacheRepository;
    }

    @Override
    public void notifyCustomerCreated(CustomerCreatedEvent event) {

        String email = findEmailByCustomerId(event.getCustomerId());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Customer Created",
                emailTemplateBuilder.customerCreated(event)
        );
    }

    @Override
    public void notifyCustomerActivated(CustomerActivatedEvent event) {

        String email = findEmailByCustomerId(event.getCustomerId());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Customer Activated",
                emailTemplateBuilder.customerActivated(event)
        );
    }

    @Override
    public void notifyCustomerDeactivated(CustomerDeactivatedEvent event) {

        String email = findEmailByCustomerId(event.getCustomerId());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Customer Deactivated",
                emailTemplateBuilder.customerDeactivated(event)
        );
    }

    @Override
    public void notifyPolicyCreated(PolicyCreatedEvent event) {

        String email = findEmailByCustomerId(event.getCustomerId());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Policy Created",
                emailTemplateBuilder.policyCreated(event)
        );

    }

    @Override
    public void notifyPolicyUpdated(PolicyUpdatedEvent event) {
        String email = findEmailByPolicyId(event.getPolicyNumber());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Policy Updated",
                emailTemplateBuilder.policyUpdated(event)
        );

    }

    @Override
    public void notifyPolicyActivated(PolicyActivatedEvent event) {

        String email = findEmailByPolicyId(event.getPolicyNumber());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Policy Activated",
                emailTemplateBuilder.policyActivated(event)
        );
    }

    @Override
    public void notifyPolicyDeactivated(PolicyDeactivatedEvent event) {

        String email = findEmailByPolicyId(event.getPolicyNumber());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Policy Deactivated",
                emailTemplateBuilder.policyDeactivated(event)
        );
    }

    @Override
    public void notifyClaimCreated(ClaimCreatedEvent event) {

        String email = findEmailByPolicyId(event.getPolicyNumber());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Claim Created",
                emailTemplateBuilder.claimCreated(event)
        );
    }

    @Override
    public void notifyClaimApproved(ClaimApprovedEvent event) {
        String email = findEmailByPolicyId(event.getPolicyNumber());
        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Claim Approved",
                emailTemplateBuilder.claimApproved(event)
        );
    }

    @Override
    public void notifyClaimRejected(ClaimRejectedEvent event) {
        String email = findEmailByPolicyId(event.getPolicyNumber());

        emailEventPublisher.publishEmailEvent(
                email,
                "ICMS - Claim Rejected",
                emailTemplateBuilder.claimRejected(event)
        );
    }

    private String findEmailByCustomerId(Integer id){
        return customerCacheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer cache doesn't exist."))
                .getEmail();

    }

    private String findEmailByPolicyId(Integer id){
        Integer customerId = policyCacheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy cache doesn't exist."))
                .getCustomerId();

        return findEmailByCustomerId(customerId);
    }

}
