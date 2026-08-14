package com.icms.claimservice.service;

import com.icms.claimservice.exception.DuplicateClaimException;
import com.icms.claimservice.exception.InvalidClaimException;
import com.icms.claimservice.exception.PolicyInactiveException;
import com.icms.claimservice.request.CreateClaimRequest;
import com.icms.claimservice.exception.ResourceNotFoundException;
import com.icms.claimservice.request.UpdateClaimRequest;
import com.icms.claimservice.response.ClaimResponse;

import java.util.List;

public interface ClaimService {

    ClaimResponse addClaim(CreateClaimRequest claim) throws ResourceNotFoundException, PolicyInactiveException, InvalidClaimException, DuplicateClaimException;

    ClaimResponse getClaimById(Integer id) throws ResourceNotFoundException;

    List<ClaimResponse> getAllClaims();

    ClaimResponse updateClaim(Integer id, UpdateClaimRequest updateClaimRequest) throws ResourceNotFoundException, InvalidClaimException, DuplicateClaimException;

    void deleteClaim(Integer id) throws ResourceNotFoundException, InvalidClaimException;

    ClaimResponse approveClaim(Integer id) throws ResourceNotFoundException, PolicyInactiveException, InvalidClaimException;

    ClaimResponse rejectClaim(Integer id) throws ResourceNotFoundException, PolicyInactiveException, InvalidClaimException;

}
