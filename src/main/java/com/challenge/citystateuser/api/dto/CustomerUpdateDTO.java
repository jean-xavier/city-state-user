package com.challenge.citystateuser.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateDTO {
    @NotBlank
    private String fullname;
}
