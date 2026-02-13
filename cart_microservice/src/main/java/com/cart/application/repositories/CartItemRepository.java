package com.cart.application.repositories;

import com.cart.application.entities.Cart;
import com.cart.application.entities.CartItem;
import com.product.application.entities.CompanyProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndCompanyProducts(
            Cart cart,
            CompanyProducts companyProducts);
}

