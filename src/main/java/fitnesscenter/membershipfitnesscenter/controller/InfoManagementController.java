package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info-management")
public class InfoManagementController {
    @Autowired
    private ParticipantService participantService;

    @PostMapping("/update-fullname")
    public ResponseEntity<String> updateFullName(@RequestParam Long participantId, @RequestParam String newFullName) {
        participantService.updateFullName(participantId, newFullName);
        return ResponseEntity.ok("Nama berhasil diperbarui.");
    }

    @PostMapping("/update-credit-card-info")
    public ResponseEntity<String> updateCreditCardInfo(@RequestParam Long participantId, @RequestParam String newCreditCardInfo) {
        participantService.updateCreditCardInfo(participantId, newCreditCardInfo);
        return ResponseEntity.ok("Informasi kartu kredit berhasil diperbarui.");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam Long participantId, @RequestParam String newPassword) {
        participantService.updatePassword(participantId, newPassword);
        return ResponseEntity.ok("Password berhasil diperbarui.");
    }
}
