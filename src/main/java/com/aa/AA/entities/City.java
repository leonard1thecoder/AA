package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @OneToOne
    private Country country;

    private String cityName;

    private String district;
    private Integer population;


}
