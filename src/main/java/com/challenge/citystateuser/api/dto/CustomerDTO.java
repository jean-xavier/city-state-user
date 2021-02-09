package com.challenge.citystateuser.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;

    @NotBlank
    private String fullname;

    @NotBlank
    private String gender;

    @NotNull
    private String birthDate;

    @NotNull
    private Integer age;

    @NotNull
    private CityDTO cityLive;

}
