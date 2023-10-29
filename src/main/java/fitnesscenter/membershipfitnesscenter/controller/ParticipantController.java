package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.service.EmailService;
import fitnesscenter.membershipfitnesscenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestParam String email, @RequestParam String otp) {
        if (paymentService.verifyPaymentOTP(email, otp)) {
            return ResponseEntity.ok("Verifikasi pembayaran berhasil.");
        } else {
            return ResponseEntity.badRequest().body("Verifikasi pembayaran gagal.");
        }
    }

    @PostMapping("/is-payment-otp-expired")
    public ResponseEntity<String> isPaymentOTPExpired(@RequestParam String email) {
        if (paymentService.isPaymentOTPExpired(email)) {
            return ResponseEntity.ok("OTP pembayaran sudah kadaluarsa.");
        } else {
            return ResponseEntity.ok("OTP pembayaran masih berlaku.");
        }
    }
    @PostMapping("/send-email-otp")
    public ResponseEntity<String> sendEmailVerification(@RequestParam String email,@RequestParam Long participantId) {
        try {
            paymentService.sendEmailVerification(email,participantId);
            return ResponseEntity.ok("Email verifikasi telah dikirim.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Gagal mengirim email verifikasi.");
        }
    }
}
