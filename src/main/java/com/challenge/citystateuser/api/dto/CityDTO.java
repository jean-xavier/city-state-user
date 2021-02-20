package com.challenge.citystateuser.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private Long id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotNull(message = "State can't be null")
    private StateDTO state;
}
