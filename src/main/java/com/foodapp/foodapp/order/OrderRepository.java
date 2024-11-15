package com.foodapp.foodapp.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByCompanyIdAndStatus(Long companyId, OrderStatus status);

    @Query("SELECT COUNT(o), DATE_TRUNC(:range, o.deliveryTime) AS timePeriod " +
            "FROM Order o " +
            "WHERE o.company.id = :companyId " +
            "AND o.deliveryTime BETWEEN :dateFrom AND :dateTo " +
            "GROUP BY timePeriod " +
            "ORDER BY timePeriod")
    List<Object[]> getOrderStatisticsChart(@Param("companyId") Long companyId,
                                           @Param("range") String range,
                                           @Param("dateFrom") LocalDateTime dateFrom,
                                           @Param("dateTo") LocalDateTime dateTo);

}