package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.DtoLoginRequest;
import fitnesscenter.membershipfitnesscenter.dto.DtoRegisterRequest;
import fitnesscenter.membershipfitnesscenter.enumeration.ParticipantStatusEnum;
import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final String SUCCESS = "Berhasil melakukan registrasi. Silakan konfirmasi melalui email Anda.";
    private static final String FAILED = "Gagal mengirim email konfirmasi.";

    @Autowired
    private EmailService emailService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody DtoRegisterRequest dtoRegisterRequest) {
        try {
            String subject = "Konfirmasi Pendaftaran";
            String text = "Terima kasih atas pendaftaran Anda di pusat kebugaran kami. Silakan klik link konfirmasi berikut untuk mengaktifkan akun Anda: [LINK KONFIRMASI]";
            emailService.sendEmail(dtoRegisterRequest.getEmail(), subject, text);
            registrationService.register(dtoRegisterRequest);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FAILED);
        }

        return ResponseEntity.ok(SUCCESS);
    }

    @GetMapping("/check-status")
    public ResponseEntity<String> checkStatus(@RequestParam String email) {
        Optional<Participant> participantOptional = participantService.findByEmail(email);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();

            if (participant.isVerified()) {
                return ResponseEntity.ok("Status kepesertaan: " + ParticipantStatusEnum.TERDAFTAR.toString());
            } else {
                return ResponseEntity.ok("Status kepesertaan: " + ParticipantStatusEnum.BELUM_TERVALIDASI.toString());
            }
        }
        return ResponseEntity.ok("Status kepesertaan: " + ParticipantStatusEnum.TIDAK_TERDAFTAR.toString());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DtoLoginRequest loginRequest) {
        try {
            AuthToken authToken = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(authToken);
        } catch (Exception e) {
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
