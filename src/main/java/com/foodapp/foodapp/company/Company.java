package com.foodapp.foodapp.company;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = true)
public class Company extends BaseEntity {
    @Id
    @SequenceGenerator(name = "company_sequence", sequenceName = "company_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
    private Integer id;
    private String name;
    private String address;
    @JdbcTypeCode(SqlTypes.JSON)
    private Content content;
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_user",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "_user_id"))
    private Set<User> companyUsers;
    @Setter
    private boolean locked;

}
