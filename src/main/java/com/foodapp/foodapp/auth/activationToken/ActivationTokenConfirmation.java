package com.foodapp.foodapp.auth.activationToken;

import com.foodapp.foodapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ActivationTokenConfirmation {
    @Id
    @SequenceGenerator(name = "activation_token_sequence", sequenceName = "activation_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activation_token_sequence")
    private long id;
    @Column(nullable = false, unique = true)
    private String activationToken;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "_user_id")
    private User user;
    private String createdBy;
    private LocalDateTime createdOn;
}
