package com.foodapp.foodapp.order.sql;

import com.foodapp.foodapp.order.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByCompany_IdInAndStatusIn(Set<Long> companyIds, List<OrderStatus> status);
    List<Order> findByCompany_IdInAndStatusIn(Set<Long> companyIds, List<OrderStatus> statuses, Sort sort);

    @Query("SELECT COUNT(o), DATE_TRUNC(:range, o.executionTime) AS timePeriod " +
            "FROM Order o " +
            "WHERE o.company.id IN (:companyIds) " +
            "AND o.executionTime BETWEEN :dateFrom AND :dateTo " +
            "AND o.status NOT IN (:statusesToExclude) " +
            "GROUP BY timePeriod " +
            "ORDER BY timePeriod")
    List<Object[]> getOrderStatisticsChart(@Param("companyIds") List<Long> companyIds,
                                           @Param("range") String range,
                                           @Param("dateFrom") LocalDateTime dateFrom,
                                           @Param("dateTo") LocalDateTime dateTo,
                                           @Param("statusesToExclude") List<OrderStatus> statusesToExclude);

    @Query("SELECT SUM(op.quantity), DATE_TRUNC(:range, o.executionTime) AS timePeriod " +
            "FROM Order o " +
            "JOIN o.orderProducts op " +
            "WHERE o.company.id IN (:companyIds) " +
            "AND (:productId IS NULL OR op.product.id = :productId) " +
            "AND o.executionTime BETWEEN :dateFrom AND :dateTo " +
            "AND o.status NOT IN (:statusesToExclude) " +
            "GROUP BY timePeriod " +
            "ORDER BY timePeriod")
    List<Object[]> getOrderStatisticsChartByProductId(@Param("companyIds") List<Long> companyIds,
                                                      @Param("range") String range,
                                                      @Param("dateFrom") LocalDateTime dateFrom,
                                                      @Param("dateTo") LocalDateTime dateTo,
                                                      @Param("productId") Long productId,
                                                      @Param("statusesToExclude") List<OrderStatus> statusesToExclude);

    @Query("SELECT COUNT(o), SUM(o.totalPrice), DATE_TRUNC(:range, o.executionTime) AS timePeriod " +
            "FROM Order o " +
            "WHERE o.company.id IN (:companyIds) " +
            "AND o.executionTime BETWEEN :dateFrom AND :dateTo " +
            "AND o.status NOT IN (:statusesToExclude) " +
            "GROUP BY timePeriod " +
            "ORDER BY timePeriod")
    List<Object[]> getOrderStatisticsChartWithEarnings(@Param("companyIds") List<Long> companyIds,
                                                       @Param("range") String range,
                                                       @Param("dateFrom") LocalDateTime dateFrom,
                                                       @Param("dateTo") LocalDateTime dateTo,
                                                       @Param("statusesToExclude") List<OrderStatus> statusesToExclude);

    @Query("SELECT SUM(op.quantity), SUM(op.price), DATE_TRUNC(:range, o.executionTime) AS timePeriod " +
            "FROM Order o " +
            "JOIN o.orderProducts op " +
            "WHERE o.company.id IN (:companyIds) " +
            "AND (:productId IS NULL OR op.product.id = :productId) " +
            "AND o.executionTime BETWEEN :dateFrom AND :dateTo " +
            "AND o.status NOT IN (:statusesToExclude) " +
            "GROUP BY timePeriod " +
            "ORDER BY timePeriod")
    List<Object[]> getOrderStatisticsChartByProductIdWithEarnings(@Param("companyIds") List<Long> companyIds,
                                                                  @Param("range") String range,
                                                                  @Param("dateFrom") LocalDateTime dateFrom,
                                                                  @Param("dateTo") LocalDateTime dateTo,
                                                                  @Param("productId") Long productId,
                                                                  @Param("statusesToExclude") List<OrderStatus> statusesToExclude);
}
