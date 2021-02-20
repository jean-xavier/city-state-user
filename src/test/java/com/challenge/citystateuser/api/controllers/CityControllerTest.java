package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CityDTO;
import com.challenge.citystateuser.api.dto.StateDTO;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.CustomerRepository;
import com.challenge.citystateuser.domain.models.repositories.StateRespository;
import com.challenge.citystateuser.domain.services.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
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

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CityController.class)
@AutoConfigureMockMvc
public class CityControllerTest {
    static final String CITY_API = "/api/cities";

    @Autowired
    MockMvc mvc;

    @MockBean
    CityService cityService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    StateRespository stateRespository;

    @MockBean
    CityRepository cityRepository;

    @Test
    @DisplayName("Deve criar uma cidade com sucesso.")
    public void createCityTest() throws Exception {
        CityDTO cityDTO = CityDTO.builder().name("Salvador").state(StateDTO.builder().id(1L).build()).build();
        City city = City.builder().id(1L).name("Salvador").state(State.builder().id(1L).build()).build();

        BDDMockito.given(cityService.save(Mockito.any(City.class))).willReturn(city);

        String json = new ObjectMapper().writeValueAsString(cityDTO);

        MockHttpServletRequestBuilder request = makePostMockHttpServletRequestBuilder(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value(cityDTO.getName()));
    }

    @Test
    @DisplayName("Deve retornar erros ao cria uma cidade com dados vazios")
    public void createInvalidCityTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new CityDTO());

        MockHttpServletRequestBuilder request = makePostMockHttpServletRequestBuilder(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(2)));
    }

    @Test
    @DisplayName("Deve retornar uma cidade pelo nome")
    public void searchCityByNameTest() throws Exception {
        final String filter = "Salvador";
        final City city = City.builder().name(filter).build();

        BDDMockito.when(cityService.findByName(Mockito.any(City.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(city), PageRequest.of(0, 100), 1));

        final String query = String.format("?city=%s&page=0&size=100", city.getName());

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(query);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect( jsonPath("content", hasSize(1)) )
                .andExpect( jsonPath("totalElements").value(1) )
                .andExpect( jsonPath( "pageable.pageSize" ).value(100) )
                .andExpect( jsonPath( "pageable.pageNumber" ).value(0) );
    }

    @Test
    @DisplayName("Deve retornar 0 elementos quando procurar uma cidade inexistente")
    public void searchInvalidCityByNameTest() throws Exception {
        final String filter = "Salvador";

        BDDMockito.when(cityService.findByName(Mockito.any(City.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<City>(Collections.emptyList(), PageRequest.of(0,100), 0));

        final String query = String.format("?city=%s&page=0&size=100", filter);

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(query);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect( jsonPath("content", hasSize(0)) )
                .andExpect( jsonPath("totalElements").value(0) )
                .andExpect( jsonPath( "pageable.pageSize" ).value(100) )
                .andExpect( jsonPath( "pageable.pageNumber" ).value(0) );
    }

    private MockHttpServletRequestBuilder makePostMockHttpServletRequestBuilder(String json) {
        return MockMvcRequestBuilders
                .post(CITY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
    }

    private MockHttpServletRequestBuilder makeGetMockHttpServletRequestBuilder(String query) {
        return MockMvcRequestBuilders
                .get(CITY_API + query)
                .accept(MediaType.APPLICATION_JSON);
    }
}
