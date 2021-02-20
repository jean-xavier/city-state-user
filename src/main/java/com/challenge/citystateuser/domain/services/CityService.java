package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.models.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CityService {
    City save(City city);

    Page<City> findByName(City city, Pageable pageable);
}
