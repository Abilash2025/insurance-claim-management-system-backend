package com.icms.claimservice.controller;

import com.icms.claimservice.exception.DuplicateClaimException;
import com.icms.claimservice.exception.InvalidClaimException;
import com.icms.claimservice.exception.PolicyInactiveException;
import com.icms.claimservice.request.CreateClaimRequest;
import com.icms.claimservice.exception.ResourceNotFoundException;
import com.icms.claimservice.request.UpdateClaimRequest;
import com.icms.claimservice.response.ClaimResponse;
import com.icms.claimservice.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("")
    public ResponseEntity<ClaimResponse> addClaim(@Valid @RequestBody CreateClaimRequest claim)
            throws ResourceNotFoundException, PolicyInactiveException,
            DuplicateClaimException, InvalidClaimException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(claimService.addClaim(claim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(@PathVariable Integer id)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClaimResponse> updateClaim(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateClaimRequest updateClaimRequest)
            throws ResourceNotFoundException, DuplicateClaimException, InvalidClaimException {
        return ResponseEntity.ok(claimService.updateClaim(id,updateClaimRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable Integer id)
            throws ResourceNotFoundException, InvalidClaimException {
        claimService.deleteClaim(id);
        return ResponseEntity.ok("Deletion successful");
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ClaimResponse> approveClaim(@PathVariable Integer id)
            throws PolicyInactiveException, InvalidClaimException,
            ResourceNotFoundException {
        return ResponseEntity.ok(claimService.approveClaim(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ClaimResponse> rejectClaim(@PathVariable Integer id)
            throws PolicyInactiveException, InvalidClaimException,
            ResourceNotFoundException {
        return ResponseEntity.ok(claimService.rejectClaim(id));
    }


}
