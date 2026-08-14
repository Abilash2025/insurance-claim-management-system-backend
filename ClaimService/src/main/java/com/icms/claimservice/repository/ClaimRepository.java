package com.icms.claimservice.repository;

import com.icms.claimservice.entity.ClaimEntity;
import com.icms.claimservice.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity,Integer> {

    @Query("SELECT SUM(c.claimAmount) " +
            "FROM ClaimEntity c " +
            "WHERE c.policyNumber = ?1 AND c.claimStatus = ?2")
    Double sumApprovedClaims(Integer policyNumber, ClaimStatus claimStatus);

    boolean existsByPolicyNumberAndClaimAmountAndClaimDate(Integer policyNumber, Double claimAmount, LocalDate claimDate);

    boolean existsByPolicyNumberAndClaimAmountAndClaimDateAndClaimNumberNot(
            Integer policyNumber,
            Double claimAmount,
            LocalDate claimDate,
            Integer claimNumber
    );
}
