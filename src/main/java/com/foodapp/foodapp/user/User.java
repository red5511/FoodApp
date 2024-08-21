package com.foodapp.foodapp.user;

import com.foodapp.foodapp.company.Company;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
@ToString
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "_user_sequence", sequenceName = "_user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_user_sequence")
    private Integer id;
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
    private String createdBy;
    private LocalDateTime createdOn;
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
