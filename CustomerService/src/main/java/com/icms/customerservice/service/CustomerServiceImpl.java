package com.icms.customerservice.service;

import com.icms.customerservice.enums.CustomerStatus;
import com.icms.customerservice.eventpublisher.CustomerEventPublisher;
import com.icms.customerservice.exception.CustomerActiveException;
import com.icms.customerservice.exception.CustomerInactiveException;
import com.icms.customerservice.request.CreateCustomerRequest;
import com.icms.customerservice.entity.CustomerEntity;
import com.icms.customerservice.exception.ResourceNotFoundException;
import com.icms.customerservice.repository.CustomerRepository;
import com.icms.customerservice.request.UpdateCustomerRequest;
import com.icms.customerservice.response.CustomerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerEventPublisher customerEventPublisher;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            CustomerEventPublisher customerEventPublisher) {
        this.customerRepository = customerRepository;
        this.customerEventPublisher = customerEventPublisher;
    }

    @Override
    @Transactional
    public CustomerResponse addCustomer(CreateCustomerRequest customer) {

        CustomerEntity customerEntity = new CustomerEntity();
        BeanUtils.copyProperties(customer,customerEntity);

        customerEntity.setCustomerStatus(CustomerStatus.ACTIVE);

        CustomerEntity savedEntity = customerRepository.save(customerEntity);

        customerEventPublisher.publishCustomerCreatedEvent(savedEntity);

        return toCustomerResponse(savedEntity);
    }

    @Override
    public CustomerResponse getCustomerById(Integer id) throws ResourceNotFoundException {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Customer with id: " + id + " doesn't exist."));

        return toCustomerResponse(customerEntity);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<CustomerEntity> customerEntityList = customerRepository.findAll();

        List<CustomerResponse> customerResponses = new ArrayList<>();

        for (CustomerEntity customerEntity : customerEntityList){
            customerResponses.add(toCustomerResponse(customerEntity));
        }

        return customerResponses;
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Integer id, UpdateCustomerRequest customer)
            throws ResourceNotFoundException, CustomerInactiveException {

        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Customer with id: " + id + " doesn't exist."));

        if(customerEntity.getCustomerStatus() == CustomerStatus.INACTIVE){
            throw new CustomerInactiveException("Cannot update an inactive customer.");
        }

        if(customer.getName() != null){
            customerEntity.setName(customer.getName());
        }

        if(customer.getEmail() != null){
            customerEntity.setEmail(customer.getEmail());
        }

        if(customer.getPhoneNumber() != null){
            customerEntity.setPhoneNumber(customer.getPhoneNumber());
        }

        if(customer.getCity() != null){
            customerEntity.setCity(customer.getCity());
        }

        CustomerEntity updatedEntity = customerRepository.save(customerEntity);

        return toCustomerResponse(updatedEntity);
    }

    @Override
    @Transactional
    public CustomerResponse deactivateCustomer(Integer id)
            throws ResourceNotFoundException, CustomerInactiveException {

        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id:" + id + " doesn't exist."));

        if(customerEntity.getCustomerStatus() == CustomerStatus.INACTIVE){
            throw new CustomerInactiveException("Customer with id: " + id + " is already inactive.");
        }

        customerEntity.setCustomerStatus(CustomerStatus.INACTIVE);
        CustomerEntity deactivatedEntity = customerRepository.save(customerEntity);

        customerEventPublisher.publishCustomerDeactivatedEvent(deactivatedEntity);

        return toCustomerResponse(deactivatedEntity);
    }

    @Override
    @Transactional
    public CustomerResponse activateCustomer(Integer id)
            throws ResourceNotFoundException, CustomerActiveException {

        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id:" + id + " doesn't exist."));

        if(customerEntity.getCustomerStatus() == CustomerStatus.ACTIVE){
            throw new CustomerActiveException("Customer with Id: " + id + " is already active.");
        }

        customerEntity.setCustomerStatus(CustomerStatus.ACTIVE);
        CustomerEntity activatedEntity = customerRepository.save(customerEntity);

        customerEventPublisher.publishCustomerActivatedEvent(activatedEntity);

        return toCustomerResponse(activatedEntity);
    }

    private CustomerResponse toCustomerResponse(CustomerEntity customerEntity){
        CustomerResponse customerResponse = new CustomerResponse();
        BeanUtils.copyProperties(customerEntity,customerResponse);
        return  customerResponse;
    }

}
