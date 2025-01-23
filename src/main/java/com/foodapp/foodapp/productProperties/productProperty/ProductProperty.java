package com.foodapp.foodapp.productProperties.productProperty;

import java.math.BigDecimal;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.productProperties.ProductProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    name = "product_property",
    uniqueConstraints = @UniqueConstraint(columnNames = {"product_properties_id", "name"})
)
public class ProductProperty extends BaseEntity {
    @Id
    @SequenceGenerator(name = "product_property_sequence", sequenceName = "product_property_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_property_sequence")
    private Long id;
    @NotNull
    @Size(max = 255)
    private String name;
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_properties_id")
    private ProductProperties productProperties;
}
