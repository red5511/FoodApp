package com.foodapp.foodapp.auth.passwordResetToken;

import com.foodapp.foodapp.common.BaseEntity;
import com.foodapp.foodapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken extends BaseEntity {

    @Id
    @SequenceGenerator(name = "password_reset_token_sequence", sequenceName = "password_reset_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_sequence")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "_user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
}
