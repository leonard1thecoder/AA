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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsLiters {
    @Id
    private Long id;

    private String productLiters;

    private String registeredDate;

    private Byte status;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private Users administrator;

    private Byte bottleReturnableStatus;

}
