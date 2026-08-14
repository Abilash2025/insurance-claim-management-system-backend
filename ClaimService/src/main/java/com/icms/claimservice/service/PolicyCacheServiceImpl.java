package com.icms.claimservice.service;

import com.icms.claimservice.entity.PolicyCacheEntity;
import com.icms.claimservice.enums.PolicyStatus;
import com.icms.claimservice.event.policy.PolicyActivatedEvent;
import com.icms.claimservice.event.policy.PolicyCreatedEvent;
import com.icms.claimservice.event.policy.PolicyDeactivatedEvent;
import com.icms.claimservice.event.policy.PolicyUpdatedEvent;
import com.icms.claimservice.exception.ResourceNotFoundException;
import com.icms.claimservice.repository.PolicyCacheRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyCacheServiceImpl implements PolicyCacheService{

    private final PolicyCacheRepository policyCacheRepository;

    public PolicyCacheServiceImpl(PolicyCacheRepository policyCacheRepository) {
        this.policyCacheRepository = policyCacheRepository;
    }

    @Override
    @Transactional
    public void addPolicyCache(PolicyCreatedEvent policyCreatedEvent){

        if (policyCacheRepository.existsById(policyCreatedEvent.getPolicyNumber())) {
            return;
        }

        PolicyCacheEntity policyCacheEntity = toPolicyCacheEntity(policyCreatedEvent);
        policyCacheRepository.save(policyCacheEntity);
    }

    @Override
    @Transactional
    public void activatePolicyCache(PolicyActivatedEvent policyActivatedEvent) throws ResourceNotFoundException {
        updateStatus(
                policyActivatedEvent.getPolicyNumber(),
                policyActivatedEvent.getStatus());
    }

    @Override
    @Transactional
    public void deactivatePolicyCache(PolicyDeactivatedEvent policyDeactivatedEvent) throws ResourceNotFoundException {
        updateStatus(
                policyDeactivatedEvent.getPolicyNumber(),
                policyDeactivatedEvent.getStatus());
    }

    @Override
    @Transactional
    public void updatePolicyCache(PolicyUpdatedEvent policyUpdatedEvent) throws ResourceNotFoundException {

        PolicyCacheEntity policyCacheEntity = findPolicyCache(policyUpdatedEvent.getPolicyNumber());

        if(policyUpdatedEvent.getPolicyType() != null){
            policyCacheEntity.setPolicyType(policyUpdatedEvent.getPolicyType());
        }
        if(policyUpdatedEvent.getCoverage() != null){
            policyCacheEntity.setCoverage(policyUpdatedEvent.getCoverage());
        }
    }

    private PolicyCacheEntity toPolicyCacheEntity(PolicyCreatedEvent policyCreatedEvent){
        PolicyCacheEntity policyCacheEntity = new PolicyCacheEntity();
        BeanUtils.copyProperties(policyCreatedEvent,policyCacheEntity);
        return policyCacheEntity;
    }

    private void updateStatus(Integer policyNumber, PolicyStatus status) throws ResourceNotFoundException {
        PolicyCacheEntity policyCacheEntity = findPolicyCache(policyNumber);
        policyCacheEntity.setStatus(status);
    }

    private PolicyCacheEntity findPolicyCache(Integer id) throws ResourceNotFoundException {
        return policyCacheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy cache not found for policy id: " + id));
    }
}
