package com.foodapp.foodapp.productCategory;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findAllByCompanyId(Long companyId, Sort sort);

    void deleteByCompanyId(Long companyId);
}
