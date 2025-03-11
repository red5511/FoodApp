package com.foodapp.foodapp.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findAllByCompanyIdIn(List<Long> companyIds);
    List<Product> findByCompanyIdAndStatus(Long companyId, ProductStatus status);

    void deleteByCompanyId(Long companyId);
}
