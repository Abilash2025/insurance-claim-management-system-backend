package com.icms.policyservice.service;

import com.icms.policyservice.entity.CustomerCacheEntity;
import com.icms.policyservice.enums.CustomerStatus;
import com.icms.policyservice.enums.PolicyStatus;
import com.icms.policyservice.eventpublisher.PolicyEventPublisher;
import com.icms.policyservice.exception.CustomerInactiveException;
import com.icms.policyservice.exception.PolicyActiveException;
import com.icms.policyservice.exception.PolicyInactiveException;
import com.icms.policyservice.repository.CustomerCacheRepository;
import com.icms.policyservice.request.CreatePolicyRequest;
import com.icms.policyservice.entity.PolicyEntity;
import com.icms.policyservice.exception.ResourceNotFoundException;
import com.icms.policyservice.repository.PolicyRepository;
import com.icms.policyservice.request.UpdatePolicyRequest;
import com.icms.policyservice.response.PolicyResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService{

    private final PolicyRepository policyRepository;
    private final CustomerCacheRepository customerCacheRepository;
    private final PolicyEventPublisher policyEventPublisher;

    public PolicyServiceImpl(PolicyRepository policyRepository,
                             CustomerCacheRepository customerCacheRepository,
                             PolicyEventPublisher policyEventPublisher) {
        this.policyRepository = policyRepository;
        this.policyEventPublisher = policyEventPublisher;
        this.customerCacheRepository = customerCacheRepository;
    }

    @Override
    @Transactional
    public PolicyResponse addPolicy(CreatePolicyRequest policy)
            throws ResourceNotFoundException, CustomerInactiveException {

        CustomerCacheEntity customerCacheEntity = customerCacheRepository.findById(policy.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " +
                        policy.getCustomerId() + " doesn't exist."));

        if(customerCacheEntity.getStatus() == CustomerStatus.INACTIVE){
            throw new CustomerInactiveException("Cannot create a policy for an inactive customer.");
        }

        PolicyEntity policyEntity = toPolicyEntity(policy);
        policyEntity.setStatus(PolicyStatus.ACTIVE);
        PolicyEntity savedPolicy = policyRepository.save(policyEntity);

        policyEventPublisher.publishPolicyCreatedEvent(savedPolicy);

        return toPolicyResponse(savedPolicy);
    }

    @Override
    public PolicyResponse getPolicyById(Integer id) throws ResourceNotFoundException {

        return toPolicyResponse(findPolicy(id));
    }

    @Override
    public List<PolicyResponse> getAllPolicies() {
        List<PolicyEntity> policyEntities = policyRepository.findAll();

        List<PolicyResponse> policyResponses = new ArrayList<>();

        for (PolicyEntity policyEntity : policyEntities){
            policyResponses.add(toPolicyResponse(policyEntity));
        }

        return policyResponses;

    }

    @Override
    @Transactional
    public PolicyResponse updatePolicy(Integer id, UpdatePolicyRequest policy)
            throws ResourceNotFoundException, PolicyInactiveException {

        PolicyEntity policyEntity = findPolicy(id);

        if(policyEntity.getStatus() == PolicyStatus.INACTIVE){
            throw new PolicyInactiveException("Cannot update an inactive policy.");
        }

        if (policy.getPolicyType() != null){
            policyEntity.setPolicyType(policy.getPolicyType());
        }
        if(policy.getPremium() != null){
            policyEntity.setPremium(policy.getPremium());
        }
        if(policy.getCoverage() != null){
            policyEntity.setCoverage(policy.getCoverage());
        }

        PolicyEntity updatedEntity = policyRepository.save(policyEntity);

        policyEventPublisher.publishPolicyUpdatedEvent(updatedEntity);

        return toPolicyResponse(updatedEntity);
    }

    @Override
    @Transactional
    public PolicyResponse deactivatePolicy(Integer id) throws ResourceNotFoundException, PolicyInactiveException {

        PolicyEntity policyEntity = findPolicy(id);

        if(policyEntity.getStatus() == PolicyStatus.INACTIVE){
            throw new PolicyInactiveException("Policy with id: " + id + " is already inactive.");
        }

        policyEntity.setStatus(PolicyStatus.INACTIVE);
        PolicyEntity deactivatedEntity = policyRepository.save(policyEntity);

        policyEventPublisher.publishPolicyDeactivatedEvent(deactivatedEntity);

        return toPolicyResponse(deactivatedEntity);

    }

    @Override
    @Transactional
    public PolicyResponse activatePolicy(Integer id)
            throws ResourceNotFoundException, PolicyActiveException, CustomerInactiveException {

        PolicyEntity policyEntity = findPolicy(id);

        CustomerCacheEntity customerCacheEntity =
                customerCacheRepository.findById(policyEntity.getCustomerId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer cache not found."));

        if(policyEntity.getStatus() == PolicyStatus.ACTIVE){
            throw new PolicyActiveException("Policy with id: " + id + " is already active");
        }

        if(customerCacheEntity.getStatus() == CustomerStatus.INACTIVE){
            throw new CustomerInactiveException("Cannot activate a policy for a inactive customer.");
        }

        policyEntity.setStatus(PolicyStatus.ACTIVE);
        PolicyEntity activatedPolicy = policyRepository.save(policyEntity);

        policyEventPublisher.publishPolicyActivatedEvent(activatedPolicy);

        return toPolicyResponse(activatedPolicy);
    }

    @Transactional
    public void deactivatePoliciesByCustomer(Integer customerId){
        List<PolicyEntity> policyEntityList = policyRepository.findPolicyEntitiesByCustomerId(customerId);
        List<PolicyEntity> updatedPolicies = new ArrayList<>();

        for(PolicyEntity policyEntity : policyEntityList){
            if(policyEntity.getStatus() == PolicyStatus.ACTIVE){
                policyEntity.setStatus(PolicyStatus.INACTIVE);
                updatedPolicies.add(policyEntity);
            }
        }
        policyRepository.saveAll(updatedPolicies);

        for (PolicyEntity policy : updatedPolicies){
            policyEventPublisher.publishPolicyDeactivatedEvent(policy);
        }
    }

    @Transactional
    public void activatePoliciesByCustomer(Integer customerId){
        List<PolicyEntity> policyEntityList = policyRepository.findPolicyEntitiesByCustomerId(customerId);
        List<PolicyEntity> updatedPolicies = new ArrayList<>();

        for(PolicyEntity policyEntity : policyEntityList){
            if(policyEntity.getStatus() == PolicyStatus.INACTIVE){
                policyEntity.setStatus(PolicyStatus.ACTIVE);
                updatedPolicies.add(policyEntity);
            }
        }

        policyRepository.saveAll(updatedPolicies);

        for (PolicyEntity policy : updatedPolicies){
            policyEventPublisher.publishPolicyActivatedEvent(policy);
        }
    }
    private PolicyResponse toPolicyResponse(PolicyEntity policyEntity){
        PolicyResponse policyResponse = new PolicyResponse();
        BeanUtils.copyProperties(policyEntity,policyResponse);
        return policyResponse;
    }

    private PolicyEntity toPolicyEntity(CreatePolicyRequest createPolicyRequest){
        PolicyEntity policyEntity = new PolicyEntity();
        BeanUtils.copyProperties(createPolicyRequest,policyEntity);
        return policyEntity;
    }

    private PolicyEntity findPolicy(Integer id) throws ResourceNotFoundException {
        return policyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Policy with " + id + " doesn't exist."));
    }
}
