package com.challenge.citystateuser.domain.services.impl;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.CustomerRepository;
import com.challenge.citystateuser.domain.services.CustomerService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, CityRepository cityRepository) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public Customer save(Customer customer) {
        Optional<City> city = cityRepository.findById(customer.getCityLive().getId());

        return city.map(cityLive -> {
            customer.setCityLive(cityLive);
            return customerRepository.save(customer);
        }).orElseThrow(() -> new BusinessException("City not found by id"));

    }

    @Override
    public Page<Customer> searchByName(Customer customer, Pageable pageable) {
        Example<Customer> example = Example.of(customer,
                ExampleMatcher.matching()
                    .withIgnoreCase()
                    .withIgnoreNullValues()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return customerRepository.findAll(example, pageable);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer update(Customer customer) {
        if (isNotCustomerValid(customer)) {
            throw new IllegalArgumentException("Customer id can't be null");
        }

        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        if (isNotCustomerValid(customer)) {
            throw new IllegalArgumentException("Customer id can't be null");
        }

        customerRepository.delete(customer);
    }

    private boolean isNotCustomerValid(Customer customer) {
        return customer == null || customer.getId() == null;
    }
}
