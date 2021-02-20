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

    @NotBlank(message = "Fullname can't be blank")
    private String fullname;

    @NotBlank(message = "Gender can't be blank")
    private String gender;

    @NotBlank(message = "Birth Date can't be blank")
    private String birthDate;

    @NotNull(message = "Age can't be null")
    private Integer age;

    @NotNull(message = "City Live can't be null")
    private CityDTO cityLive;

}
