package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ActivationTokenConfirmationService tokenConfirmationService;

    @PostMapping("/register/init")
    public ResponseEntity<AuthenticationResponse> register(final @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    @GetMapping("/register/confirm/{token}") //todo czy to powinien byc Get??
    public ResponseEntity<String> activateUser(final @PathVariable String token) {
        tokenConfirmationService.confirmToken(token);
        return ResponseEntity.ok("Confirmed");
    }

    @PostMapping("password/change/init")
    public ResponseEntity<String> initPasswordChange(final @RequestParam("email") String email){
        authenticationService.initPasswordChange(email);
        return ResponseEntity.ok("Email with resting request was send to your email");
    }

    @GetMapping("password/change/confirm/{token}")
    public String changePassword(final @PathVariable("token") String token){
        return "Load up frontend stuff";
    }

    @PutMapping("password/change/confirm")
    public ResponseEntity<String> changePassword(final @RequestParam("token") String token,
                                                 final @RequestParam("newPassword") String newPassword){
        authenticationService.confirmPasswordChange(token, newPassword);
        return ResponseEntity.ok("Your password was changed correctly!");
    }

}
