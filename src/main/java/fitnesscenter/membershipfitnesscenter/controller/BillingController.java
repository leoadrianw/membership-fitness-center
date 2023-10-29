package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/billing")
public class BillingController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/verify-bill")
    public ResponseEntity<String> verifyBillAmount(@RequestParam Long participantId,@RequestParam BigDecimal expectedBillAmount) {
        if (paymentService.verifyBillAmount(participantId,expectedBillAmount)) {
            return ResponseEntity.ok("Verifikasi tagihan berhasil.");
        } else {
            return ResponseEntity.badRequest().body("Verifikasi tagihan gagal.");
        }
    }
}
