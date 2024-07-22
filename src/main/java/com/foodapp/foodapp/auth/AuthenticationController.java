package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.auth.token.ActivationTokenConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ActivationTokenConfirmationService tokenConfirmationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(final @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    @GetMapping("register/confirm/{token}")
    public ResponseEntity<String> activateUser(final @PathVariable String token) {
        tokenConfirmationService.confirmToken(token);
        return ResponseEntity.ok("Confirmed");
    }

}
