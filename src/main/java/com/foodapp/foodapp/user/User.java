package com.foodapp.foodapp.user;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.order.sql.OrderStatusConverter;
import com.foodapp.foodapp.user.permission.Permission;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
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
    private String phoneNumber;
    private boolean marketingConsent;
    @Convert(converter = RoleConverter.class)
    private Role role;
    @Builder.Default
    @Setter
    private Boolean locked = false;
    @Builder.Default
    @Setter
    private Boolean enabled = false;
    @ToString.Exclude
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Company> companies;
    @ElementCollection(fetch = FetchType.EAGER)
    @Convert(converter = PermissionConverter.class)
    private Set<Permission> permissions;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        // Add permissions to authorities
        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.name()));
        }
        return authorities;    }

    @Override
    public String getUsername() {
        return email;
    }
}
