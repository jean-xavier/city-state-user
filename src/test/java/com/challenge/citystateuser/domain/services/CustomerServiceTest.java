package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.CustomerRepository;
import com.challenge.citystateuser.domain.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CustomerServiceTest {

    CustomerService customerService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    CityRepository cityRepository;

    @BeforeEach
    public void setUp() {
        this.customerService = new CustomerServiceImpl(customerRepository, cityRepository);
    }

    @Test
    @DisplayName("Deve salvar um novo cliente")
    public void saveCustomerTest() {
        City city = City.builder().id(1L).name("Salvador").build();
        Customer customer = Customer.builder().fullname("Fulano").age(29).cityLive(city).build();
        Customer mock = Customer.builder().id(1L).fullname("Fulano").age(29).cityLive(city).build();

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(city));
        Mockito.when(customerRepository.save(customer)).thenReturn(mock);

        Customer savedCustomer = customerService.save(customer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getFullname()).isEqualTo(mock.getFullname());
        assertThat(savedCustomer.getAge()).isEqualTo(mock.getAge());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar um usuario com city inexistente")
    public void saveInvalidCustomerTest() {
        City city = City.builder().id(1L).name("Salvador").build();
        Customer customer = Customer.builder().fullname("Fulano").age(29).cityLive(city).build();

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> customerService.save(customer));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("City not found by id");

        Mockito.verify(customerRepository, Mockito.never()).save(customer);
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo nome")
    public void searchCustomerByNameTest() {
        final Customer customer = Customer.builder().fullname("Fulano").build();
        final PageRequest pageRequest = PageRequest.of(0, 10);

        List<Customer> customers = Collections.singletonList(customer);
        Page<Customer> pages = new PageImpl<>(customers, pageRequest, 1);

        Mockito.when(customerRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(pages);

        Page<Customer> customerFound = customerService.searchByName(customer, pageRequest);

        assertThat(customerFound.getTotalElements()).isEqualTo(1);
        assertThat(customerFound.getContent()).isEqualTo(customers);
        assertThat(customerFound.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(customerFound.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo id")
    public void findCustomerByIdTest() {
        final Long id = 1L;

        Customer mock = Customer.builder().id(1L).fullname("Fulano").age(29)
                .cityLive(City.builder().id(1L).name("Salvador").build()).build();

        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(mock));

        Customer customer = customerService.findById(id).get();

        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getFullname()).isEqualTo(mock.getFullname());
        assertThat(customer.getAge()).isEqualTo(mock.getAge());
        assertThat(customer.getCityLive().getName()).isEqualTo(mock.getCityLive().getName());
    }

    @Test
    @DisplayName("Deve retornar um optional vazio quando não encontrar um cliente pelo id")
    public void findInexistentCustomerByidTest() {
        final Long id = 1L;

        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Customer> customer = customerService.findById(id);

        assertThat(customer.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar os dados de um usuario")
    public void updateCustomerTest() {
        Customer customer = Customer.builder().id(1L).fullname("Fulano").build();

        Customer mock = Customer.builder().id(1L).fullname("Sicrano").age(29)
                .cityLive(City.builder().id(1L).name("Salvador").build()).build();

        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.save(customer)).thenReturn(mock);

        Customer updatedCustomer = customerService.update(customer);

        assertThat(updatedCustomer.getId()).isNotNull();
        assertThat(updatedCustomer.getFullname()).isEqualTo(mock.getFullname());
        assertThat(updatedCustomer.getAge()).isEqualTo(mock.getAge());
        assertThat(updatedCustomer.getCityLive().getName()).isEqualTo(mock.getCityLive().getName());
    }

    @Test
    @DisplayName("Deve lançar uma exception quando o id não for encontrado")
    public void updateInvalidCustomerTest() {
        Customer customer = Customer.builder().build();

        Throwable exception = catchThrowable(() -> customerService.update(customer));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer id can't be null");

        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any(Customer.class));
    }
}
