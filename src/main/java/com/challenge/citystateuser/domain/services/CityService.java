package com.challenge.citystateuser.domain.services;

import com.challenge.citystateuser.domain.models.entities.City;
import org.springframework.stereotype.Service;

@Service
public interface CityService {
    City save(City city);
}
