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

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Id do estado é obrigatório")
    private StateDTO state;
}
