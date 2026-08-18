package com.icms.policyservice.repository;

import com.icms.policyservice.entity.CustomerCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCacheRepository extends JpaRepository<CustomerCacheEntity,Integer> {
}
