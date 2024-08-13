package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import com.foodapp.foodapp.auth.request.AuthenticationRequest;
import com.foodapp.foodapp.auth.request.ChangeInitPasswordRequest;
import com.foodapp.foodapp.auth.request.RegisterRequest;
import com.foodapp.foodapp.auth.response.AuthenticationResponse;
import com.foodapp.foodapp.auth.response.ChangeInitPasswordResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ActivationTokenConfirmationService tokenConfirmationService;

    @PostMapping("/register/init")
    public ResponseEntity<AuthenticationResponse> register(final @RequestBody @Valid RegisterRequest request) throws BusinessException {
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
    public ResponseEntity<ChangeInitPasswordResponse> initPasswordChange(final @RequestBody ChangeInitPasswordRequest requset) {
        authenticationService.initPasswordChange(requset.getEmail());
        return ResponseEntity.ok(ChangeInitPasswordResponse.of(true));
    }

    @GetMapping("password/change/confirm/{token}")
    public String changePassword(final @PathVariable("token") String token) {
        return "Load up frontend stuff";
    }

    @PutMapping("password/change/confirm")
    public ResponseEntity<String> changePassword(final @RequestParam("token") String token,
                                                 final @RequestParam("newPassword") String newPassword) {
        authenticationService.confirmPasswordChange(token, newPassword);
        return ResponseEntity.ok("Your password was changed correctly!");
    }

}
