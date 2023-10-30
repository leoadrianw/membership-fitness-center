package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.DtoBillRequest;
import fitnesscenter.membershipfitnesscenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/verify-bill")
    public ResponseEntity<String> verifyBillAmount(@RequestHeader("Authorization") String authToken, @RequestBody DtoBillRequest dtoBillRequest) {
        if (paymentService.verifyBillAmount(authToken, dtoBillRequest.getExpectedBillAmount())) {
            return ResponseEntity.ok("Verifikasi tagihan berhasil.");
        } else {
            return ResponseEntity.badRequest().body("Verifikasi tagihan gagal.");
        }
    }
}
