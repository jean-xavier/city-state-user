package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CustomerDTO;
import com.challenge.citystateuser.api.dto.CustomerUpdateDTO;
import com.challenge.citystateuser.api.dto.FilterCustomerDTO;
import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<CustomerDTO> searchByName(@Valid FilterCustomerDTO filterCustomerDTO, Pageable pageable) {
        Customer customer = Customer.builder().fullname(filterCustomerDTO.getName()).build();

        Page<Customer> customers = customerService.searchByName(customer, pageable);

        List<CustomerDTO> customerDTOS = customers.getContent().stream()
                .map(entity -> modelMapper.map(entity, CustomerDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(customerDTOS, pageable, customerDTOS.size());
    }

    @GetMapping("/{id}")
    public CustomerDTO searchById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public CustomerDTO update(@PathVariable Long id, @RequestBody @Valid CustomerUpdateDTO dto) {
        return customerService.findById(id)
                .map(customer -> {
                    customer.setFullname(dto.getFullname());
                    customer = customerService.update(customer);
                    return modelMapper.map(customer, CustomerDTO.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);

        if (customer.isPresent()) {
            customerService.delete(customer.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
