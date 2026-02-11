package com.product.application.repositories;

import com.product.application.entities.ProductsLiters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLitersRepository extends JpaRepository<ProductsLiters,Long> {

    Optional<ProductsLiters> findByProductLiters(String productLiters);

}
