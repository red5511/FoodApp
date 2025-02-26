package com.foodapp.foodapp.delivery;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(
        name = "delivery_option",
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "distance"})
)
public class DeliveryOption extends BaseEntity {
    @Id
    @SequenceGenerator(name = "delivery_option_sequence", sequenceName = "delivery_option_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_option_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @ToString.Exclude
    private Company company;
    private BigDecimal deliveryPrice;
    private Float distance;
}
