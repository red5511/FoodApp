package com.foodapp.foodapp.company;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

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
    @Column(columnDefinition = "jsonb")
    private Content content;
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;
    @Builder.Default
    @Setter
    private Boolean locked = false;
    private String createdBy;
    private LocalDateTime createdOn;

}
