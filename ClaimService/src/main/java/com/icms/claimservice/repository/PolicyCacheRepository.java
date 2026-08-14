package com.icms.claimservice.repository;

import com.icms.claimservice.entity.PolicyCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyCacheRepository extends JpaRepository<PolicyCacheEntity,Integer> {
}
