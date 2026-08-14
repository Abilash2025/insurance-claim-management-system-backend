package com.icms.claimservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "POLICY-SERVICE")
public interface PolicyClient {

//    @GetMapping("/policy/getPolicyById/{id}")
//    PolicyCacheResponse getPolicyById(@PathVariable Integer id);
}
