package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.StateRespository;
import com.challenge.citystateuser.domain.services.impl.CityServiceImpl;
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
public class CityServiceTest {

    CityService cityService;

    @MockBean
    CityRepository cityRepository;

    @MockBean
    StateRespository stateRespository;

    @BeforeEach
    public void setUp() {
        this.cityService = new CityServiceImpl(cityRepository, stateRespository);
    }

    @Test
    @DisplayName("Deve salvar uma cidade")
    public void saveCityTest() {
        State state = State.builder().id(1L).build();
        City city = City.builder().name("Salvador").state(state).build();
        City mock = City.builder().id(1L).name("Salvador").state(State.builder().id(1L).name("Bahia").build()).build();

        Mockito.when(stateRespository.findById(Mockito.any())).thenReturn(Optional.of(state));
        Mockito.when(cityRepository.save(city)).thenReturn(mock);

        City savedCity = cityService.save(city);

        assertThat(savedCity.getId()).isNotNull();
        assertThat(savedCity.getName()).isEqualTo("Salvador");
        assertThat(savedCity.getState()).isNotNull();
        assertThat(savedCity.getState().getName()).isEqualTo("Bahia");
    }

    @Test
    @DisplayName("Deve lançar erro ao adicionar um cidade com um state inexistente.")
    public void shouldNotSaveExistsCityTest() {
        final City city = City.builder().name("Salvador").state(State.builder().id(1L).build()).build();

        Mockito.when(stateRespository.findById(Mockito.any())).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> cityService.save(city));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("State not found");

        Mockito.verify(cityRepository, Mockito.never()).save(city);
    }

    @Test
    @DisplayName("Deve encontrar um cidade pelo nome")
    public void findCityByNameTest() {
        final City city = City.builder().id(1L).name("Salvador").build();
        final PageRequest pageRequest = PageRequest.of(0, 10);

        List<City> cities = Collections.singletonList(city);
        Page<City> pages = new PageImpl<>(cities, pageRequest, 1);

        Mockito.when(cityRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(pages);

        Page<City> citiesFound = cityService.findByName(city, pageRequest);

        assertThat(citiesFound.getTotalElements()).isEqualTo(1);
        assertThat(citiesFound.getContent()).isEqualTo(cities);
        assertThat(citiesFound.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(citiesFound.getPageable().getPageSize()).isEqualTo(10);
    }
}
