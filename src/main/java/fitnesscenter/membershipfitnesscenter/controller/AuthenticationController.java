package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.DtoLoginRequest;
import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.service.AuthenticationService;
import fitnesscenter.membershipfitnesscenter.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DtoLoginRequest loginRequest) {
        try {
            AuthToken authToken = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(authToken);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authToken) {
        authenticationService.logout(authToken);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String authToken) {
        AuthToken newAuthToken = authenticationService.refreshToken(authToken);
        if (newAuthToken != null) {
            return ResponseEntity.ok(newAuthToken);
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            passwordResetService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset email sent.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password reset request failed: " + e.getMessage());
        }
    }
}
