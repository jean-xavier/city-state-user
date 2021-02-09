package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CustomerDTO;
import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO create(@RequestBody @Valid CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);

        customer = customerService.save(customer);

        return modelMapper.map(customer, CustomerDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CustomerDTO searchByName(@RequestParam("name") String name) {
        return customerService.searchByName(name)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not found user with name '%s'.", name)));
    }
}
