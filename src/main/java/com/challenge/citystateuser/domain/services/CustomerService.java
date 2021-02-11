package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.models.entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustomerService {
    Customer save(Customer customer);

    Optional<Customer> searchByName(String fullname);

    Optional<Customer> findById(Long id);

    Optional<Customer> update(Customer customer);

    void delete(Customer customer);
}
