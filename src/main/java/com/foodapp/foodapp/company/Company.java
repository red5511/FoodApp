package com.foodapp.foodapp.company;

import com.foodapp.foodapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Company {
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
            joinColumns = @JoinColumn(name = "_user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private Set<User> companyUsers;
    @Setter
    private boolean locked;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

}
