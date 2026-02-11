package com.product.application.repositories;

import com.product.application.entities.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductListRepository extends JpaRepository<ProductList,Long> {

    Optional <ProductList> findByProductName(String productName);

}
