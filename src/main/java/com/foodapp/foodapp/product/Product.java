package com.foodapp.foodapp.product;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    private String name;
    private BigDecimal price;
    private String imgUrl;
    private String description;
    private boolean soldOut;
}
