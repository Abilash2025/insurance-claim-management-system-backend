package com.icms.notificationservice.service;

import com.icms.notificationservice.entity.PolicyCacheEntity;
import com.icms.notificationservice.enums.PolicyStatus;
import com.icms.notificationservice.event.policy.PolicyActivatedEvent;
import com.icms.notificationservice.event.policy.PolicyCreatedEvent;
import com.icms.notificationservice.event.policy.PolicyDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyUpdatedEvent;
import com.icms.notificationservice.exception.ResourceNotFoundException;
import com.icms.notificationservice.repository.PolicyCacheRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class policyCacheServiceImpl implements PolicyCacheService{

    private final PolicyCacheRepository policyCacheRepository;

    public policyCacheServiceImpl(PolicyCacheRepository policyCacheRepository) {
        this.policyCacheRepository = policyCacheRepository;
    }

    @Override
    @Transactional
    public void addPolicyCache(PolicyCreatedEvent event) {
        PolicyCacheEntity policyCacheEntity = new PolicyCacheEntity();
        BeanUtils.copyProperties(event,policyCacheEntity);
        policyCacheRepository.save(policyCacheEntity);
    }

    @Override
    @Transactional
    public void updatePolicyCache(PolicyUpdatedEvent event) {
        PolicyCacheEntity policyCacheEntity = findPolicy(event.getPolicyNumber());

        if(event.getPolicyType() != null){
          policyCacheEntity.setPolicyType(event.getPolicyType());
        }

        if(event.getCoverage() != null){
            policyCacheEntity.setCoverage(event.getCoverage());
        }

        if (event.getPremium() != null){
            policyCacheEntity.setPremium(event.getPremium());
        }
    }

    @Override
    @Transactional
    public void activatePolicyCache(PolicyActivatedEvent event) {
        updateStatus(event.getPolicyNumber(),event.getStatus());
    }

    @Override
    @Transactional
    public void deactivatePolicyCache(PolicyDeactivatedEvent event) {
        updateStatus(event.getPolicyNumber(),event.getStatus());
    }

    private PolicyCacheEntity findPolicy(Integer id){
        return policyCacheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy cache doesn't exist."));
    }

    private void updateStatus(Integer id, PolicyStatus status){
        PolicyCacheEntity policyCacheEntity = findPolicy(id);
        policyCacheEntity.setStatus(status);
    }

}
