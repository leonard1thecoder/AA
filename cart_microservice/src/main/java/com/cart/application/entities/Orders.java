package com.cart.application.entities;

import com.users.application.entities.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    private Users user;

    private Double totalAmount;

    private String orderDate;

    private String status;
}

