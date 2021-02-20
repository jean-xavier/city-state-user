package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CityDTO;
import com.challenge.citystateuser.api.dto.FilterCityDTO;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.services.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/cities")
public class CityController {

    private final CityService cityService;
    private final ModelMapper modelMapper;

    public CityController(CityService cityService) {
        this.cityService = cityService;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityDTO create(@RequestBody @Valid CityDTO cityDTO) {
        City city = modelMapper.map(cityDTO, City.class);

        city = cityService.save(city);

        return modelMapper.map(city, CityDTO.class);
    }

    @GetMapping
    public Page<CityDTO> find(@Valid FilterCityDTO filterCityDTO, Pageable pageable) {
        final State state = State.builder().name(filterCityDTO.getState()).build();
        final City city = City.builder().name(filterCityDTO.getCity()).state(state).build();

        Page<City> cities = cityService.findByName(city, pageable);

        List<CityDTO> cityDTOS = cities.getContent().stream()
                .map(entity -> modelMapper.map(entity, CityDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(cityDTOS, pageable, cityDTOS.size());
    }
}
