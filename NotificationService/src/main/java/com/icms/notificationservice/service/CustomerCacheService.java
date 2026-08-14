package com.icms.notificationservice.service;

import com.icms.notificationservice.event.customer.CustomerActivatedEvent;
import com.icms.notificationservice.event.customer.CustomerCreatedEvent;
import com.icms.notificationservice.event.customer.CustomerDeactivatedEvent;

public interface CustomerCacheService {

    void addCustomerCache(CustomerCreatedEvent event);

    void activateCustomerCache(CustomerActivatedEvent event);

    void deactivateCustomerCache(CustomerDeactivatedEvent event);
}
