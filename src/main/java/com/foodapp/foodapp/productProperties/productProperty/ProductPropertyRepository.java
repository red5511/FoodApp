package com.foodapp.foodapp.productProperties.productProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {
}
