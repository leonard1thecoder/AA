package com.product.application.repositories;

import com.product.application.entities.CompanyProducts;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyProductsRepository extends JpaRepository<CompanyProducts, Long> {

    List<CompanyProducts> findByCompanyProduct_owner_Id(Long companyId);

    List<CompanyProducts> findByCompanyProductStatus(Byte status);

    Optional<CompanyProducts> findByCompanyProduct_owner_IdAndProductList_Id(
            Long companyId, Long productListId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cp FROM CompanyProducts cp WHERE cp.companyProductId = :id")
    Optional<CompanyProducts> findByIdForUpdate(@Param("id") Long id);
}
