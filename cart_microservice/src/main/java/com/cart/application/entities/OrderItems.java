package com.cart.application.entities;

import com.product.application.entities.CompanyProducts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Orders order;

    @ManyToOne
    private CompanyProducts companyProducts;

    private Integer quantity;

    private Double priceAtPurchase;
}
