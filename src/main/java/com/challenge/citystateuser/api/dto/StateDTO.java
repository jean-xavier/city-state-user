package com.challenge.citystateuser.api.dto;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StateDTO {
    private Long id;
    private String name;
    private Set<CityDTO> cities;
}
