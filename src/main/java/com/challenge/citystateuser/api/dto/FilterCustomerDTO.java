package com.challenge.citystateuser.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCustomerDTO {
    @NotBlank(message = "Name can't be blank")
    private String name;
}
