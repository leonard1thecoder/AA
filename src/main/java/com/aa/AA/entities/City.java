package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class City {

    @Id
    @GeneratedValue
    private Integer cityId;

    @JoinColumn(name="code",nullable = false)
    @OneToMany
    private Country country;

    private String cityName;

    private Integer population;


}
