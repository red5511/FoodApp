package com.foodapp.foodapp.user;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.administration.company.Company;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = true)
public class User extends BaseEntity implements UserDetails {
    @Id
    @SequenceGenerator(name = "_user_sequence", sequenceName = "_user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_user_sequence")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder.Default
    @Setter
    private Boolean locked = false;
    @Builder.Default
    @Setter
    private Boolean enabled = false;
    @ManyToMany(mappedBy = "companyUsers", fetch = FetchType.EAGER)
    private Set<Company> companies;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
