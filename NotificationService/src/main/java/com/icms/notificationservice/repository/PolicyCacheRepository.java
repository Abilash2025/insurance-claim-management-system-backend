package com.icms.notificationservice.repository;

import com.icms.notificationservice.entity.PolicyCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyCacheRepository extends JpaRepository<PolicyCacheEntity,Integer> {
}
