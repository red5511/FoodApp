package com.foodapp.foodapp.orderProduct;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.order.Order;
import com.foodapp.foodapp.product.Product;
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

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_products")
@ToString
public class OrderProduct extends BaseEntity {

    @Id
    @SequenceGenerator(name = "order_product_sequence", sequenceName = "order_product_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_product_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @JdbcTypeCode(SqlTypes.JSON)
    private OrderProductContent content;

    @Size(max = 510)
    @Column(length = 510)
    private String note;

}
