package com.foodapp.foodapp.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCompanyId(final Long companyId);
    List<Order> findByCompanyIdAndStatus(Long companyId, OrderStatus status);
}
