package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(
        name = "product_properties",
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "name"})
)
public class ProductProperties extends BaseEntity {
    @Id
    @SequenceGenerator(name = "product_properties_sequence", sequenceName = "product_properties_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_properties_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @OneToMany(mappedBy = "productProperties", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductProperty> productPropertyList;
    @ManyToMany(mappedBy = "productPropertiesList")
    private List<Product> products;
    @NotNull
    @Size(max = 255)
    private String name;
    private boolean required;
    @Builder.Default
    private Integer maxChosenOptions = 1;
}
