package com.challenge.citystateuser.domain.services.impl;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.services.CityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        final Boolean exists = cityRepository.existsByName(city.getName());

        if (exists) {
            throw new BusinessException("City already exists");
        }

        return cityRepository.save(city);
    }

    @Override
    public Optional<City> findByName(String name) {
        return cityRepository.findByName(name);
    }
}
