package com.challenge.citystateuser.domain.models.entities;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private Long id;
    private String name;
    private Long idState;
}
