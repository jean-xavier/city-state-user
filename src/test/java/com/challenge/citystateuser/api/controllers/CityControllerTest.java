package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CityDTO;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.services.CityService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        MockHttpServletRequestBuilder request = makeMockHttpServletRequestBuilder(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value(cityDTO.getName()));
    }

    @Test
    @DisplayName("Deve retornar erros ao cria uma cidade com dados vazios")
    public void createInvalidCity() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new CityDTO());

        MockHttpServletRequestBuilder request = makeMockHttpServletRequestBuilder(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(2)));
    }

    private MockHttpServletRequestBuilder makeMockHttpServletRequestBuilder(String json) {
        return MockMvcRequestBuilders
                .post(CITY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
    }
}
