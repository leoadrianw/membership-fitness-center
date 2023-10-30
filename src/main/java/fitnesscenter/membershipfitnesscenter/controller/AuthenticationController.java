package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.*;
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
            String text = "Terima kasih atas pendaftaran Anda di pusat kebugaran kami. Berikut Nomor OTP untuk megaktifkan akun Anda:";
            String verificationCode = emailService.sendEmail(dtoRegisterRequest.getEmail(), subject, text);
            registrationService.register(dtoRegisterRequest, verificationCode);
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
            DtoLoginResponse response = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Autentikasi Gagal: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authToken) {
        authenticationService.logout(authToken);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> login(@RequestHeader("Authorization") String authToken, @RequestBody DtoVerificationRequest dtoVerificationRequest) {
        boolean validVerificationCode = registrationService.validateOtp(authToken, dtoVerificationRequest);
        if (validVerificationCode) {
            return ResponseEntity.ok("Akun Anda Berhasil Divalidasi. Status kepesertaan: " + ParticipantStatusEnum.TERDAFTAR.toString());
        }
        return ResponseEntity.ok("Kode OTP yang Anda masukkan tidak sesuai.");
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
    public ResponseEntity<?> forgotPassword(@RequestBody DtoEmailRequest dtoEmailRequest) {
        try {
            boolean isPasswordReset = passwordResetService.requestPasswordReset(dtoEmailRequest);

            if (isPasswordReset) {
                return ResponseEntity.ok("Password reset email berhasil dikirim. Silahkan cek email Anda.");
            }

            return ResponseEntity.badRequest().body("Password reset email gagal dikirim.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password reset request gagal dikirim: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password-confirmation")
    public ResponseEntity<?> forgotPasswordConfirmation(@RequestBody DtoForgotPasswordRequest dtoForgotPasswordRequest) {
        if (!dtoForgotPasswordRequest.getNewPassword().equals(dtoForgotPasswordRequest.getConfirmNewPassword())) {
            return ResponseEntity.badRequest().body("Password tidak sesuai.");
        }

        boolean isPasswordReset = passwordResetService.confirmPasswordReset(dtoForgotPasswordRequest);

        if (isPasswordReset) {
            return ResponseEntity.ok("Password berhasil diubah.");
        }

        return ResponseEntity.badRequest().body("Password gagal diubah, periksa kembali token yang anda masukkan");
    }
}
