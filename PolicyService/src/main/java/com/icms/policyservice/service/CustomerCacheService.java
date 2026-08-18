package com.icms.policyservice.service;

import com.icms.policyservice.event.customer.CustomerActivatedEvent;
import com.icms.policyservice.event.customer.CustomerCreatedEvent;
import com.icms.policyservice.event.customer.CustomerDeactivatedEvent;

public interface CustomerCacheService {

    void addCustomerCache(CustomerCreatedEvent customerCreatedEvent);

    void deactivateCustomer(CustomerDeactivatedEvent customerDeactivatedEvent);

    void activateCustomer(CustomerActivatedEvent customerActivatedEvent);
}
