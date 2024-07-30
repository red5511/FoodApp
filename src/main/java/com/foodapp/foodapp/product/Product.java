package com.foodapp.foodapp.product;

import com.foodapp.foodapp.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private Integer id;
    private String name;
    private String price;
    private String imgUrl;
    private String description;
    private boolean soldOut;
    @JoinColumn(nullable = false, name = "company_id")
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
}
