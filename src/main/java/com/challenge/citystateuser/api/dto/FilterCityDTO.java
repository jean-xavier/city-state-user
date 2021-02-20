package com.challenge.citystateuser.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCityDTO {
    @NotBlank(message = "City can't be blank")
    private String city;

    private String state;
}
