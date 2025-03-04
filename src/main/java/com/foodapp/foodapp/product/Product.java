package com.foodapp.foodapp.product;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.productCategory.ProductCategory;
import com.foodapp.foodapp.productProperties.ProductProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product extends BaseEntity {
    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    public ProductStatus status = ProductStatus.ACTIVE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @NotNull
    @Size(max = 255)
    private String name;
    private BigDecimal price;
    private BigDecimal deliveryPrice;
    private BigDecimal takeawayPrice;
    private String imgUrl;
    private String description;
    private boolean soldOut;
    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
    @ManyToMany
    @JoinTable(
            name = "product_product_properties", // Join table name
            joinColumns = @JoinColumn(name = "product_id"),  // Column for the Product side of the relationship
            inverseJoinColumns = @JoinColumn(name = "product_properties_id") // Column for ProductProperties side
    )
    private List<ProductProperties> productPropertiesList;
}
