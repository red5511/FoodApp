package com.foodapp.foodapp.order;

import com.foodapp.foodapp.administration.company.Content;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.administration.company.Company;
import com.foodapp.foodapp.product.Product;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@ToString
public class Order extends BaseEntity {
    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    private BigDecimal price;
    private String description;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String deliveryAddress;
    private String customerName;
    private LocalDateTime deliveryTime;
    private LocalDateTime approvalDeadline;
    @JdbcTypeCode(SqlTypes.JSON)
    private OrderContent content;
}
