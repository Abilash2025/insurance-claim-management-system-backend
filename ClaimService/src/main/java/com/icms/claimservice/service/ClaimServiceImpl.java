package com.icms.claimservice.service;

import com.icms.claimservice.entity.PolicyCacheEntity;
import com.icms.claimservice.enums.ClaimStatus;
import com.icms.claimservice.enums.PolicyStatus;
import com.icms.claimservice.eventpublisher.ClaimEventPublisher;
import com.icms.claimservice.exception.DuplicateClaimException;
import com.icms.claimservice.exception.InvalidClaimException;
import com.icms.claimservice.exception.PolicyInactiveException;
import com.icms.claimservice.repository.PolicyCacheRepository;
import com.icms.claimservice.request.CreateClaimRequest;

import com.icms.claimservice.entity.ClaimEntity;
import com.icms.claimservice.exception.ResourceNotFoundException;
import com.icms.claimservice.repository.ClaimRepository;
import com.icms.claimservice.request.UpdateClaimRequest;
import com.icms.claimservice.response.ClaimResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimServiceImpl implements ClaimService{

    private final ClaimRepository claimRepository;
    private final PolicyCacheRepository policyCacheRepository;
    private final ClaimEventPublisher claimEventPublisher;

    public ClaimServiceImpl(ClaimRepository claimRepository,
                            PolicyCacheRepository policyCacheRepository,
                            ClaimEventPublisher claimEventPublisher) {
        this.claimRepository = claimRepository;
        this.policyCacheRepository = policyCacheRepository;
        this.claimEventPublisher = claimEventPublisher;
    }

    @Override
    @Transactional
    public ClaimResponse addClaim(CreateClaimRequest claim)
            throws ResourceNotFoundException, PolicyInactiveException, InvalidClaimException, DuplicateClaimException {

        PolicyCacheEntity policyCacheEntity = findPolicyCacheEntity(claim.getPolicyNumber());

        if(policyCacheEntity.getStatus() == PolicyStatus.INACTIVE){
            throw new PolicyInactiveException("Cannot create a claim for inactive policy");
        }

        validateCoverage(claim.getClaimAmount(), policyCacheEntity);

        if(claimRepository.existsByPolicyNumberAndClaimAmountAndClaimDate(
                claim.getPolicyNumber(),
                claim.getClaimAmount(),
                claim.getClaimDate()
        )){
            throw new DuplicateClaimException("Claim already exists");
        }

        ClaimEntity claimEntity = toClaimEntity(claim);
        claimEntity.setClaimStatus(ClaimStatus.PENDING);

        ClaimEntity savedEntity = claimRepository.save(claimEntity);

        claimEventPublisher.publishClaimCreatedEvent(savedEntity);

        return toClaimResponse(savedEntity);
    }

    @Override
    public ClaimResponse getClaimById(Integer id) throws ResourceNotFoundException {
        return toClaimResponse(findClaim(id));
    }

    @Override
    public List<ClaimResponse> getAllClaims() {
        List<ClaimEntity> claimEntities = claimRepository.findAll();

        List<ClaimResponse> claimResponses = new ArrayList<>();

        for(ClaimEntity claimEntity : claimEntities){
            claimResponses.add(toClaimResponse(claimEntity));
        }

        return claimResponses;
    }

    @Override
    @Transactional
    public ClaimResponse updateClaim(Integer id, UpdateClaimRequest updateClaimRequest) throws ResourceNotFoundException, InvalidClaimException, DuplicateClaimException {

        ClaimEntity claimEntity = findClaim(id);

        PolicyCacheEntity policyCacheEntity = findPolicyCacheEntity(claimEntity.getPolicyNumber());

        if(policyCacheEntity.getStatus() == PolicyStatus.INACTIVE){
            throw new InvalidClaimException(
                    "Claims of inactive policies cannot be updated.");
        }

        if (claimEntity.getClaimStatus() != ClaimStatus.PENDING) {
            throw new InvalidClaimException(
                    "Only pending claims can be updated.");
        }

        Double claimAmount = updateClaimRequest.getClaimAmount() != null
                ? updateClaimRequest.getClaimAmount()
                : claimEntity.getClaimAmount();

        LocalDate claimDate = updateClaimRequest.getClaimDate() != null
                ? updateClaimRequest.getClaimDate()
                : claimEntity.getClaimDate();

        if (claimRepository.existsByPolicyNumberAndClaimAmountAndClaimDateAndClaimNumberNot(
                claimEntity.getPolicyNumber(),
                claimAmount,
                claimDate,
                claimEntity.getClaimNumber())) {

            throw new DuplicateClaimException("A similar claim already exists.");
        }

        if(updateClaimRequest.getClaimAmount() != null){
            validateCoverage(updateClaimRequest.getClaimAmount(),policyCacheEntity);
            claimEntity.setClaimAmount(updateClaimRequest.getClaimAmount());
        }

        if(updateClaimRequest.getClaimDate() != null){
            claimEntity.setClaimDate(updateClaimRequest.getClaimDate());
        }

        ClaimEntity updatedEntity = claimRepository.save(claimEntity);

        return toClaimResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteClaim(Integer id) throws ResourceNotFoundException, InvalidClaimException {

        ClaimEntity claimEntity = findClaim(id);

        if(claimEntity.getClaimStatus() != ClaimStatus.PENDING){
            throw new InvalidClaimException("Deletion of Approved or Rejected claims is not possible.");
        }

        claimRepository.delete(claimEntity);
    }

    @Override
    @Transactional
    public ClaimResponse approveClaim(Integer id) throws ResourceNotFoundException, PolicyInactiveException, InvalidClaimException {

        ClaimEntity claimEntity = findClaim(id);

        PolicyCacheEntity policyCacheEntity = findPolicyCacheEntity(claimEntity.getPolicyNumber());

        if(policyCacheEntity.getStatus() == PolicyStatus.INACTIVE){
            throw new PolicyInactiveException(
                    "Cannot approve a claim for inactive policy.");
        }

        if(claimEntity.getClaimStatus() == ClaimStatus.REJECTED){
            throw new InvalidClaimException(
                    "Cannot approve a rejected claim.");
        }

        if (claimEntity.getClaimStatus() == ClaimStatus.APPROVED) {
            throw new InvalidClaimException(
                    "Claim is already approved.");
        }

        if(claimEntity.getClaimAmount() > policyCacheEntity.getCoverage()){
            throw new InvalidClaimException(
                    "Cannot approve: Claim amount exceeds policy coverage.");
        }

       validateCoverage(claimEntity.getClaimAmount(),policyCacheEntity);

        claimEntity.setClaimStatus(ClaimStatus.APPROVED);
        ClaimEntity approvedEntity = claimRepository.save(claimEntity);

        claimEventPublisher.publishClaimApprovedEvent(approvedEntity);

        return toClaimResponse(approvedEntity);
    }

    @Override
    @Transactional
    public ClaimResponse rejectClaim(Integer id) throws ResourceNotFoundException, PolicyInactiveException, InvalidClaimException {
        ClaimEntity claimEntity = findClaim(id);

        PolicyCacheEntity policyCacheEntity = findPolicyCacheEntity(claimEntity.getPolicyNumber());

        if(policyCacheEntity.getStatus() == PolicyStatus.INACTIVE){
            throw new PolicyInactiveException("Cannot reject a claim for inactive policy.");
        }

        if(claimEntity.getClaimStatus() == ClaimStatus.REJECTED){
            throw new InvalidClaimException("Claim is already rejected");
        }

        if(claimEntity.getClaimStatus() == ClaimStatus.APPROVED){
            throw new InvalidClaimException("Cannot reject an approved claim.");
        }

        claimEntity.setClaimStatus(ClaimStatus.REJECTED);
        ClaimEntity rejectedClaim = claimRepository.save(claimEntity);

        claimEventPublisher.publishClaimRejectedEvent(rejectedClaim);

        return toClaimResponse(rejectedClaim);
    }

    private void validateCoverage(Double requestClaimAmount, PolicyCacheEntity policyCacheEntity) throws InvalidClaimException {

        if(requestClaimAmount > policyCacheEntity.getCoverage()){
            throw new InvalidClaimException("Claim amount exceeds policy coverage.");
        }

        double approvedAmount =
                Optional.ofNullable(
                        claimRepository.sumApprovedClaims(policyCacheEntity.getPolicyNumber(),ClaimStatus.APPROVED))
                .orElse(0.0);

        if( approvedAmount + requestClaimAmount > policyCacheEntity.getCoverage()){
            throw new InvalidClaimException("Claim amount exceeds the policy coverage.");
        }

    }
    private ClaimEntity toClaimEntity(CreateClaimRequest claimBean){
        ClaimEntity claimEntity = new ClaimEntity();
        BeanUtils.copyProperties(claimBean,claimEntity);
        return claimEntity;
    }

    private ClaimResponse toClaimResponse(ClaimEntity claimEntity){
        ClaimResponse claimResponse = new ClaimResponse();
        BeanUtils.copyProperties(claimEntity,claimResponse);
        return claimResponse;
    }

    private ClaimEntity findClaim(Integer id)
            throws ResourceNotFoundException {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim with id: "+ id + " doesn't exist."));
    }

    private PolicyCacheEntity findPolicyCacheEntity(Integer id)
            throws ResourceNotFoundException {
        return policyCacheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy with id: " + id + " doesn't exist." ));
    }
}