package com.aa.AA.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class RetailCompanyStockSizeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Short stockSizeId;

    private Short sizeInMilliLiter;

}
