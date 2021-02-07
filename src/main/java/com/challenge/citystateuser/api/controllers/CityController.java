package com.challenge.citystateuser.api.controllers;

import com.challenge.citystateuser.api.dto.CityDTO;
import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.services.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
    @ResponseStatus(HttpStatus.FOUND)
    public CityDTO find(@RequestParam("name") String name) {
        return cityService.findByName(name)
                .map(city -> modelMapper.map(city, CityDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found by " + name));
    }
}
