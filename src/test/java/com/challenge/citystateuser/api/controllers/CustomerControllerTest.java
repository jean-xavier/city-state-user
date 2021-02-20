package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CityDTO;
import com.challenge.citystateuser.api.dto.CustomerDTO;
import com.challenge.citystateuser.api.dto.CustomerUpdateDTO;
import com.challenge.citystateuser.api.dto.StateDTO;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.CustomerRepository;
import com.challenge.citystateuser.domain.models.repositories.StateRespository;
import com.challenge.citystateuser.domain.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {
    static final String CUSTOMER_API = "/api/customers";

    @Autowired
    MockMvc mvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    CityRepository cityRepository;

    @MockBean
    StateRespository stateRespository;


    @Test
    @DisplayName("Deve criar um novo cliente com sucesso.")
    public void createCustomerTest() throws Exception {
        final CustomerDTO customerDTO = makeNewCustomerDTO();
        final Customer customer = makeNewCustomer();

        BDDMockito.when(customerService.save(Mockito.any(Customer.class))).thenReturn(customer);

        String json = new ObjectMapper().writeValueAsString(customerDTO);

        MockHttpServletRequestBuilder request = makePostMockHttpServletRequestBuilder(json);

        mvc.perform(request)
                .andDo(System.out::println)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("fullname").value(customer.getFullname()))
                .andExpect(jsonPath("age").value(customer.getAge()))
                .andExpect(jsonPath("gender").value(customer.getGender()))
                .andExpect(jsonPath("birthDate").value(customer.getBirthDate().toString()))
                .andExpect(jsonPath("cityLive.name").value(customer.getCityLive().getName()));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar criar um usuario com dados vazios")
    public void createInvalidCityTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new CustomerDTO());

        MockHttpServletRequestBuilder request = makePostMockHttpServletRequestBuilder(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));
    }

    @Test
    @DisplayName("Deve encontrar um usuario pelo nome")
    public void searchUserByNameTest() throws Exception {
        final Customer customer = makeNewCustomer();

        BDDMockito.when(customerService.searchByName(Mockito.any(Customer.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<Customer>(Collections.singletonList(customer), PageRequest.of(0,100), 1));

        final String query = String.format("?name=%s&page=0&size=100", customer.getFullname());

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(query);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect( jsonPath("content", hasSize(1)) )
                .andExpect( jsonPath("totalElements").value(1) )
                .andExpect( jsonPath( "pageable.pageSize" ).value(100) )
                .andExpect( jsonPath( "pageable.pageNumber" ).value(0) );
    }

    @Test
    @DisplayName("Deve retornar 0 elementos quando não encontrar um usuario pelo nome")
    public void notFoundUserWhenSearchByNameTest() throws Exception {
        final String name = "Fulano";

        BDDMockito.when(customerService.searchByName(Mockito.any(Customer.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<Customer>(Collections.emptyList(), PageRequest.of(0,100), 0));

        final String query = String.format("?name=%s&page=0&size=100", name);

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(query);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect( jsonPath("content", hasSize(0)) )
                .andExpect( jsonPath("totalElements").value(0) )
                .andExpect( jsonPath( "pageable.pageSize" ).value(100) )
                .andExpect( jsonPath( "pageable.pageNumber" ).value(0) );
    }

    @Test
    @DisplayName("Deve encontrar um usuario pelo seu id")
    public void foundUserByIdTest() throws Exception {
        final Customer customer = makeNewCustomer();

        BDDMockito.when(customerService.findById(customer.getId())).thenReturn(Optional.of(customer));

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder("/1");

        mvc.perform(request)
                .andExpect(status().isFound())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("fullname").value(customer.getFullname()))
                .andExpect(jsonPath("age").value(customer.getAge()))
                .andExpect(jsonPath("gender").value(customer.getGender()))
                .andExpect(jsonPath("birthDate").value(customer.getBirthDate().toString()))
                .andExpect(jsonPath("cityLive.name").value(customer.getCityLive().getName()));
    }

    @Test
    @DisplayName("Deve retornar 404 quando não encontrar um usuario pelo id")
    public void notFoundUserWhenSearchByIdTest() throws Exception {
        final String path = "/2";

        BDDMockito.when(customerService.findById(2L)).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(path);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve alterar nome do cliente")
    public void updateUserNameTest() throws Exception {
        final Long id = 1L;

        final Customer customerFound = makeNewCustomer();
        final Customer customerUpdated = Customer.builder().fullname("João Santos").build();

        final CustomerUpdateDTO customerUpdateDTO = CustomerUpdateDTO.builder().fullname("João Santos").build();
        final String json = new ObjectMapper().writeValueAsString(customerUpdateDTO);

        BDDMockito.when(customerService.findById(id)).thenReturn(Optional.of(customerFound));
        BDDMockito.when(customerService.update(Mockito.any(Customer.class))).thenReturn(customerUpdated);

        MockHttpServletRequestBuilder request = makePatchMockHttpServletRequestBuilder(id, json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("fullname").value(customerUpdated.getFullname()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar alterar o nome de um cliente inexistente")
    public void updateInvalidUserNameTest() throws Exception {
        final Long id = 5L;

        final CustomerUpdateDTO customerUpdateDTO = CustomerUpdateDTO.builder().fullname("João Santos").build();

        final String json = new ObjectMapper().writeValueAsString(customerUpdateDTO);

        BDDMockito.when(customerService.update(Mockito.any(Customer.class))).thenReturn(null);

        MockHttpServletRequestBuilder request = makePatchMockHttpServletRequestBuilder(id, json);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    public void delUserByIdTest() throws Exception {
        final Long id = 1L;

        Customer customer = makeNewCustomer();

        BDDMockito.given(customerService.findById(id)).willReturn(Optional.of(customer));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_API.concat("/" + id));

        mvc.perform(request).andExpect(status().isNoContent());

        BDDMockito.verify(customerService, Mockito.times(1)).delete(Mockito.any(Customer.class));
    }

    @Test
    @DisplayName("Deve lançar 404 quando tentar exlcuir um usuário inexistente")
    public void delInvalidUserByIdTest() throws Exception {
        final Long id = 1L;

        BDDMockito.given(customerService.findById(id)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_API.concat("/" + id));

        mvc.perform(request).andExpect(status().isNotFound());

        BDDMockito.verify(customerService, Mockito.never()).delete(Mockito.any(Customer.class));
    }

    private MockHttpServletRequestBuilder makePostMockHttpServletRequestBuilder(String json) {
        return MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
    }

    private MockHttpServletRequestBuilder makeGetMockHttpServletRequestBuilder(String query) {
        return MockMvcRequestBuilders
                .get(CUSTOMER_API + query)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder makePatchMockHttpServletRequestBuilder(Long resource, String json) {
        return MockMvcRequestBuilders
                .patch(CUSTOMER_API.concat("/" + resource))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    private CustomerDTO makeNewCustomerDTO() {
        final StateDTO stateDTO = StateDTO.builder().id(1L).name("Bahia").build();
        final CityDTO cityDTO = CityDTO.builder().id(1L).name("Salvador").state(stateDTO).build();

        return CustomerDTO.builder().fullname("Jean santos").age(24).birthDate("1996-07-11").
                gender("Male").cityLive(cityDTO).build();
    }

    private Customer makeNewCustomer() {
        final State state = State.builder().id(1L).name("Bahia").build();
        final City city = City.builder().id(1L).name("Salvador").state(state).build();

        return Customer.builder().id(1L).fullname("Jean Santos").cityLive(city)
                .age(24).birthDate(LocalDate.parse("1996-07-11")).gender("Male").build();
    }
}
