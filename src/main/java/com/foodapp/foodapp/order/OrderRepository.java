package com.foodapp.foodapp.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByCompanyId(final Long companyId);

    List<Order> findByCompanyIdAndStatus(Long companyId, OrderStatus status);

    @Query("""
                SELECT o FROM Order o
                WHERE o.company.id = :companyId
                AND (:date IS NULL OR DATE(o.deliveryTime) = :date)
            """)
    Page<Order> searchOrders3(
            @Param("companyId") Long companyId,
            @Param("date") String date,
            Pageable pageable
    );


    @Query("""
            SELECT o FROM Order o
            WHERE o.company.id = :companyId
            AND (COALESCE(:date, NULL) IS NULL OR DATE(o.deliveryTime) = COALESCE(:date, DATE(o.deliveryTime)))
                            """)
    List<Order> searchOrders2(
            @Param("companyId") Long companyId,
            @Param("date") LocalDate date);

}
//          AND (:name IS NULL OR CAST(o.id AS string) LIKE LOWER(CONCAT('%', :name, '%')))
//       AND (:date IS NULL OR FUNCTION('DATE', o.deliveryTime) = :date)