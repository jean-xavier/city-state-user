package com.challenge.citystateuser.domain.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tab_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private String gender;
    private LocalDate birthDate;
    private Integer age;

    @ManyToOne
    private City cityLive;

}
