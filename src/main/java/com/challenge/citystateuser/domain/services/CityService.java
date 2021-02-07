package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.models.entities.City;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CityService {
    City save(City city);

    Optional<City> findByName(String name);
}
