package com.cart.application.entities;

import com.product.application.entities.CompanyProducts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "company_product_id", nullable = false)
    private CompanyProducts companyProducts;

    private Integer quantity;

    private Double priceSnapshot; // store price at time of adding
}
