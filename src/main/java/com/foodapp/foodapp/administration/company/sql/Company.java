package com.foodapp.foodapp.administration.company.sql;

import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.delivery.DeliveryOption;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@ToString
public class Company extends BaseEntity {
    @Id
    @SequenceGenerator(name = "company_sequence", sequenceName = "company_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
    private Long id;
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    private Content content;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_user",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "_user_id"))
    @ToString.Exclude
    private Set<User> users;
    @Setter
    private boolean locked;
    @Setter
    private String webSocketTopicName;
    @JdbcTypeCode(SqlTypes.JSON)
    private Address address;
    private String defaultProductImgUrl;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryOption> deliveryOptions;
}
