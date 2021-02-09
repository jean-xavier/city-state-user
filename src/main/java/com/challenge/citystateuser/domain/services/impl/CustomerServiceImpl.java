package com.challenge.citystateuser.domain.services.impl;

import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public Customer save(Customer customer) {
        return null;
    }

    @Override
    public Optional<Customer> searchByName(String fullname) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.empty();
    }
}
