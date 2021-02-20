package com.challenge.citystateuser.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Set<CityDTO> cities;
}
