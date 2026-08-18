package com.icms.policyservice.service;

import com.icms.policyservice.entity.CustomerCacheEntity;
import com.icms.policyservice.enums.CustomerStatus;
import com.icms.policyservice.event.customer.CustomerActivatedEvent;
import com.icms.policyservice.event.customer.CustomerCreatedEvent;
import com.icms.policyservice.event.customer.CustomerDeactivatedEvent;
import com.icms.policyservice.repository.CustomerCacheRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerCacheServiceImpl implements CustomerCacheService{

    private final CustomerCacheRepository customerCacheRepository;

    private final PolicyService policyService;

    public CustomerCacheServiceImpl(
            CustomerCacheRepository customerCacheRepository,
            PolicyService policyService) {
        this.customerCacheRepository = customerCacheRepository;
        this.policyService = policyService;
    }

    @Override
    @Transactional
    public void addCustomerCache(CustomerCreatedEvent customerCreatedEvent) {
        CustomerCacheEntity customerCacheEntity = new CustomerCacheEntity(
                customerCreatedEvent.getCustomerId(),
                customerCreatedEvent.getStatus()
        );

        customerCacheRepository.save(customerCacheEntity);
    }

    @Override
    @Transactional
    public void deactivateCustomer(CustomerDeactivatedEvent customerDeactivatedEvent){
        updateStatus(
                customerDeactivatedEvent.getCustomerId(),
                customerDeactivatedEvent.getStatus()
        );

        policyService.deactivatePoliciesByCustomer(customerDeactivatedEvent.getCustomerId());
    }

    @Override
    @Transactional
    public void activateCustomer(CustomerActivatedEvent customerActivatedEvent) {
        updateStatus(
                customerActivatedEvent.getCustomerId(),
                customerActivatedEvent.getStatus()
        );
        policyService.activatePoliciesByCustomer(customerActivatedEvent.getCustomerId());
    }

    private void updateStatus(Integer id, CustomerStatus status){
        CustomerCacheEntity customerCacheEntity = customerCacheRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Customer cache not found for customer id " + id));

        customerCacheEntity.setStatus(status);
        customerCacheRepository.save(customerCacheEntity);
    }
}
