package com.foodapp.foodapp.delivery;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOptionRepository extends JpaRepository<DeliveryOption, Long> {
    List<DeliveryOption> findAllByCompanyId(Long companyId, Sort sort);
}
