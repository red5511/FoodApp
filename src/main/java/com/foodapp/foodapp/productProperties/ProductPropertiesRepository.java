package com.foodapp.foodapp.productProperties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPropertiesRepository extends JpaRepository<ProductProperties, Long> {
    List<ProductProperties> findAllByCompanyId(Long companyId);

}
