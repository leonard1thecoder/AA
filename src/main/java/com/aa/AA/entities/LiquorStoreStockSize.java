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
public class LiquorStoreStockSize {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pkStockSizeId;

    private Short sizeInMilliLiter;

}
