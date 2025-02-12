package com.foodapp.foodapp.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.orderProduct.OrderProduct;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
    private String deliveryCode;
    private BigDecimal price;
    private String description;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String deliveryAddress;
    private String customerName;
    private LocalDateTime executionTime;
    private LocalDateTime approvalDeadline;
    private boolean takeaway;
    //    @JdbcTypeCode(SqlTypes.JSON)
    //    private OrderContent content;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;
}
