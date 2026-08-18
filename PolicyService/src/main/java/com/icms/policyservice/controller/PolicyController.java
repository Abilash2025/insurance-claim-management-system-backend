package com.icms.policyservice.controller;

import com.icms.policyservice.exception.CustomerInactiveException;
import com.icms.policyservice.exception.PolicyActiveException;
import com.icms.policyservice.exception.PolicyInactiveException;
import com.icms.policyservice.request.CreatePolicyRequest;
import com.icms.policyservice.exception.ResourceNotFoundException;
import com.icms.policyservice.request.UpdatePolicyRequest;
import com.icms.policyservice.response.PolicyResponse;
import com.icms.policyservice.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("")
    public ResponseEntity<PolicyResponse> addPolicy(@Valid @RequestBody CreatePolicyRequest policy)
            throws ResourceNotFoundException, CustomerInactiveException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(policyService.addPolicy(policy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable Integer id)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePolicyRequest request)
            throws ResourceNotFoundException, PolicyInactiveException {
        return ResponseEntity.ok(policyService.updatePolicy(id,request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PolicyResponse> deactivatePolicy(@PathVariable Integer id)
            throws PolicyInactiveException, ResourceNotFoundException {
        return ResponseEntity.ok(policyService.deactivatePolicy(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PolicyResponse> activatePolicy(@PathVariable Integer id)
            throws PolicyActiveException, ResourceNotFoundException,
            CustomerInactiveException {
        return ResponseEntity.ok(policyService.activatePolicy(id));
    }
}
