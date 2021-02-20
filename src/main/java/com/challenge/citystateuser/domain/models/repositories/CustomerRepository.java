package com.challenge.citystateuser.domain.models.repositories;

import com.challenge.citystateuser.domain.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByFullname(String name);
}
