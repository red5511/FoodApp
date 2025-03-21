package com.foodapp.foodapp.order.sql;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.OrderType;
import com.foodapp.foodapp.order.PaymentMethod;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "displayable_id", unique = false)
    private Long displayableId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    private Long parentId;
    private BigDecimal totalPrice;
    private BigDecimal foodPrice;
    @Size(max = 510)
    @Column(length = 510)
    private String description;
    private OrderType orderType;
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentMethod;
    private String customerName;
    private LocalDateTime executionTime;
    private LocalDateTime approvalDeadline;
    private boolean takeaway;
    private boolean paidWhenOrdered; //opłacone przy tworzeniu ordera
    //    @JdbcTypeCode(SqlTypes.JSON)
    //    private OrderContent content;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;
    @JdbcTypeCode(SqlTypes.JSON)
    private Address deliveryAddress;
    private String deliveryCode;
    private BigDecimal deliveryPrice;
    private boolean delivery;
    private String deliveryNote;
}
