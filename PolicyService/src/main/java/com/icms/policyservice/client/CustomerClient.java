package com.icms.policyservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {

}
