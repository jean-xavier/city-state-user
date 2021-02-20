package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.models.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustomerService {
    Customer save(Customer customer);

    Page<Customer> searchByName(Customer customer, Pageable pageable);

    Optional<Customer> findById(Long id);

    Customer update(Customer customer);

    void delete(Customer customer);
}
