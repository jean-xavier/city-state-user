package com.challenge.citystateuser.domain.services.impl;

import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.services.CityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    @Override
    public City save(City city) {
        return null;
    }

    @Override
    public Optional<City> findByName(String name) {
        return Optional.empty();
    }
}
