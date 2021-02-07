package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CityDTO;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.services.CityService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

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

    @Test
    @DisplayName("Deve criar uma cidade com sucesso.")
    public void createCityTest() throws Exception {
        CityDTO cityDTO = CityDTO.builder().name("Salvador").idState(1L).build();
        City city = City.builder().id(1L).name("Salvador").idState(1L).build();

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
        City city = City.builder().id(1L).name("Salvador").idState(1L).build();

        BDDMockito.when(cityService.findByName(city.getName())).thenReturn(Optional.of(city));

        final String query = String.format("?name=%s", city.getName());

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(query);

        mvc.perform(request)
                .andExpect(status().isFound())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value(city.getName()));
    }

    @Test
    @DisplayName("Deve lançar erro quando procurar uma cidade inexistente")
    public void searchInvalidCityByNameTest() throws Exception {
        City city = City.builder().id(1L).name("Salvador").idState(1L).build();

        BDDMockito.when(cityService.findByName(city.getName())).thenReturn(Optional.empty());

        final String query = String.format("?name=%s", city.getName());

        MockHttpServletRequestBuilder request = makeGetMockHttpServletRequestBuilder(query);

        mvc.perform(request).andExpect(status().isNotFound());
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
