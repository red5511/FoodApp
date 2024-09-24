package com.foodapp.foodapp.auth.activationToken;

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
public class ActivationTokenConfirmation extends BaseEntity {
    @Id
    @SequenceGenerator(name = "activation_token_sequence", sequenceName = "activation_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activation_token_sequence")
    private Long id;
    @Column(nullable = false, unique = true)
    private String activationToken;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "_user_id")
    private User user;
}
