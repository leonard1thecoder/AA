package com.aa.AA.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Country {

    @Id
    @GeneratedValue
    private String code;
    
    private String countryName;
    
    private String countryRegion;
    
    private Integer population;
    
    private String continent;
    
    private Double surfaceArea;
    
    private Integer indepYear;
    
    private Double lifeExpectancy;
    
    private Double gnp;
    
    private Double oldGnp;
    
    private String localName;
    
    private String governmentForm;
    
    private String headOfState;
    
    private String capital;
    
    private String code2;

}
