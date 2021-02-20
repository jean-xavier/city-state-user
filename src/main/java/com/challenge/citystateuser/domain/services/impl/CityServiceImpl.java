package com.challenge.citystateuser.domain.services.impl;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.StateRespository;
import com.challenge.citystateuser.domain.services.CityService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final StateRespository stateRespository;

    public CityServiceImpl(CityRepository cityRepository, StateRespository stateRespository) {
        this.cityRepository = cityRepository;
        this.stateRespository = stateRespository;
    }

    @Override
    public City save(City city) {
        Optional<State> state = stateRespository.findById(city.getState().getId());

        return state.map(stateFound -> {
            city.setState(stateFound);
            return cityRepository.save(city);
        }).orElseThrow(() -> new BusinessException("State not found"));
    }

    @Override
    public Page<City> findByName(City city, Pageable pageable) {
        Example<City> example = Example.of(city,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.EXACT));

        return cityRepository.findAll(example, pageable);
    }
}
