package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.services.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CityServiceTest {

    CityService cityService;

    @MockBean
    CityRepository cityRepository;

    @BeforeEach
    public void setUp() {
        this.cityService = new CityServiceImpl(cityRepository);
    }

    @Test
    @DisplayName("Deve salvar uma cidade")
    public void saveCityTest() {
        City city = City.builder().name("Salvador").state(State.builder().id(1L).build()).build();
        City mock = City.builder().id(1L).name("Salvador").state(State.builder().id(1L).name("Bahia").build()).build();

        Mockito.when(cityRepository.existsByName(city.getName())).thenReturn(false);
        Mockito.when(cityRepository.save(city)).thenReturn(mock);

        City savedCity = cityService.save(city);

        assertThat(savedCity.getId()).isNotNull();
        assertThat(savedCity.getName()).isEqualTo("Salvador");
        assertThat(savedCity.getState()).isNotNull();
        assertThat(savedCity.getState().getName()).isEqualTo("Bahia");
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar uma cidade existente.")
    public void shouldNotSaveExistsCityTest() {
        final City city = City.builder().name("Salvador").state(State.builder().id(1L).build()).build();

        Mockito.when(cityRepository.existsByName(city.getName())).thenReturn(true);

        Throwable exception = catchThrowable(() -> cityService.save(city));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("City already exists");

        Mockito.verify(cityRepository, Mockito.never()).save(city);
    }

    @Test
    @DisplayName("Deve encontrar um cidade pelo nome")
    public void findCityByNameTest() {
        String name = "Salvador";
        City mock = City.builder().id(1L).name("Salvador").state(State.builder().id(1L).name("Bahia").build()).build();

        Mockito.when(cityRepository.findByName(name)).thenReturn(Optional.of(mock));

        City city = cityService.findByName(name).get();

        assertThat(city.getName()).isEqualTo(mock.getName());
        assertThat(city.getId()).isEqualTo(mock.getId());
        assertThat(city.getState()).isEqualTo(mock.getState());
    }

    @Test
    @DisplayName("Deve retornar empty quando não encontrar uma cidade pelo nome")
    public void notFindCityByNameTest() {
        String name = "Salvador";

        Mockito.when(cityRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<City> city = cityService.findByName(name);

        assertThat(city.isPresent()).isFalse();
    }
}
