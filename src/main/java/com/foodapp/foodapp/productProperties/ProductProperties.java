package com.foodapp.foodapp.productProperties;

import java.util.List;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
}
