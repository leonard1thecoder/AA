package com.aa.AA.entities;

import jakarta.persistence.Column;
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
/*
    * Remove unused column
 */
public class Country {

    @Id
    @GeneratedValue
    @Column(insertable=false, updatable=false)
    private String code;
    
    private String countryName;
    
    private String countryRegion;
    
    private Integer population;
    
    private String continent;
    
    private Double surfaceArea;
    
    private Short indepYear;
    
    private Double lifeExpectancy;
    
    private Double gnp;
    
    private Double oldGnp;
    
    private String localName;
    
    private String governmentForm;
    
    private String headOfState;
    
    private String capital;
    
    private String code2;

}
