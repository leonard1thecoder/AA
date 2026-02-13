package com.cart.application.repositories;

import com.cart.application.entities.Cart;
import com.users.application.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find cart by user entity
    Optional<Cart> findByUser(Users user);

    // Find cart by userId (used in checkout)
    Optional<Cart> findByUser_UserId(Long userId);

    // Optional: fetch with items eagerly (avoids lazy loading issues)
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.user.userId = :userId")
    Optional<Cart> findCartWithItems(@Param("userId") Long userId);
}

