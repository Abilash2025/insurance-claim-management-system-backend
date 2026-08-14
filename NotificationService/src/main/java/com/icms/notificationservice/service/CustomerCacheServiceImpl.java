package com.icms.notificationservice.service;

import com.icms.notificationservice.entity.CustomerCacheEntity;
import com.icms.notificationservice.enums.CustomerStatus;
import com.icms.notificationservice.event.customer.CustomerActivatedEvent;
import com.icms.notificationservice.event.customer.CustomerCreatedEvent;
import com.icms.notificationservice.event.customer.CustomerDeactivatedEvent;
import com.icms.notificationservice.exception.ResourceNotFoundException;
import com.icms.notificationservice.repository.CustomerCacheRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerCacheServiceImpl implements CustomerCacheService{

    private final CustomerCacheRepository customerCacheRepository;

    public CustomerCacheServiceImpl(CustomerCacheRepository customerCacheRepository) {
        this.customerCacheRepository = customerCacheRepository;
    }

    @Override
    @Transactional
    public void addCustomerCache(CustomerCreatedEvent event) {
        CustomerCacheEntity customerCacheEntity = new CustomerCacheEntity();
        BeanUtils.copyProperties(event,customerCacheEntity);
        customerCacheRepository.save(customerCacheEntity);
    }

    @Override
    @Transactional
    public void activateCustomerCache(CustomerActivatedEvent event) {
        updateStatus(event.getCustomerId(),event.getStatus());
    }

    @Override
    @Transactional
    public void deactivateCustomerCache(CustomerDeactivatedEvent event) {
        updateStatus(event.getCustomerId(),event.getStatus());
    }

    private void updateStatus(Integer id, CustomerStatus status){
        CustomerCacheEntity customerCacheEntity = customerCacheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer cache doesn't exist."));
        customerCacheEntity.setStatus(status);
    }
}
