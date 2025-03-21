package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(
        name = "product_category",
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "name"})
)
public class ProductCategory extends BaseEntity {
    @Id
    @SequenceGenerator(name = "product_category_sequence", sequenceName = "product_category_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    private String name;
    private Integer sortOrder;
    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Product> products;
}
