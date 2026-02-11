package com.product.application.entities;

import com.users.application.entities.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class ProductList {
    @Id
    private long productId;

    private String productName,productImagePath;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private Users administrator;

    private byte productStatus;

}
