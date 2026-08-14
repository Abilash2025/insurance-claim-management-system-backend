package com.icms.customerservice.service;

import com.icms.customerservice.exception.CustomerActiveException;
import com.icms.customerservice.exception.CustomerInactiveException;
import com.icms.customerservice.request.CreateCustomerRequest;
import com.icms.customerservice.exception.ResourceNotFoundException;
import com.icms.customerservice.request.UpdateCustomerRequest;
import com.icms.customerservice.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse addCustomer(CreateCustomerRequest customer);

    CustomerResponse getCustomerById(Integer id) throws ResourceNotFoundException;

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(Integer id, UpdateCustomerRequest customer) throws ResourceNotFoundException, CustomerInactiveException;

    CustomerResponse deactivateCustomer(Integer id) throws ResourceNotFoundException, CustomerInactiveException;

    CustomerResponse activateCustomer(Integer id) throws ResourceNotFoundException, CustomerActiveException;
}
