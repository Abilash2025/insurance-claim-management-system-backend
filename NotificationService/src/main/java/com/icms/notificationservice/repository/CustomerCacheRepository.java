package com.icms.notificationservice.repository;

import com.icms.notificationservice.entity.CustomerCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCacheRepository extends JpaRepository<CustomerCacheEntity,Integer> {
}
