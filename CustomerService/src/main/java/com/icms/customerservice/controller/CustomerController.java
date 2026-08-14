package com.icms.customerservice.controller;

import com.icms.customerservice.exception.CustomerActiveException;
import com.icms.customerservice.exception.CustomerInactiveException;
import com.icms.customerservice.request.CreateCustomerRequest;
import com.icms.customerservice.exception.ResourceNotFoundException;
import com.icms.customerservice.request.UpdateCustomerRequest;
import com.icms.customerservice.response.CustomerResponse;
import com.icms.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<CustomerResponse> addCustomer(
            @Valid @RequestBody CreateCustomerRequest customer){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.addCustomer(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Integer id)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCustomerRequest customer)
            throws ResourceNotFoundException, CustomerInactiveException {

        return ResponseEntity.ok(customerService.updateCustomer(id,customer));

    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerResponse> deactivateCustomer(@PathVariable Integer id)
            throws ResourceNotFoundException, CustomerInactiveException {

        return ResponseEntity.ok(customerService.deactivateCustomer(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerResponse> activateCustomer(@PathVariable Integer id)
            throws CustomerActiveException, ResourceNotFoundException {

        return ResponseEntity.ok(customerService.activateCustomer(id));
    }
}
