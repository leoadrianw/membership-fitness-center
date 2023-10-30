package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.DtoUpdateInfoManagementRequest;
import fitnesscenter.membershipfitnesscenter.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info-management")
public class InfoManagementController {
    @Autowired
    private ParticipantService participantService;

    @PostMapping("/update-fullname")
    public ResponseEntity<String> updateFullName(@RequestHeader("Authorization") String authToken, @RequestBody DtoUpdateInfoManagementRequest dtoUpdateInfoManagementRequest) {
        participantService.updateFullName(authToken, dtoUpdateInfoManagementRequest.getNewFullName());
        return ResponseEntity.ok("Nama berhasil diperbarui.");
    }

    @PostMapping("/update-credit-card-info")
    public ResponseEntity<String> updateCreditCardInfo(@RequestHeader("Authorization") String authToken, @RequestBody DtoUpdateInfoManagementRequest dtoUpdateInfoManagementRequest) {
        participantService.updateCreditCardInfo(authToken, dtoUpdateInfoManagementRequest.getNewCreditCardInfo());
        return ResponseEntity.ok("Informasi kartu kredit berhasil diperbarui.");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authToken, @RequestBody DtoUpdateInfoManagementRequest dtoUpdateInfoManagementRequest) {
        participantService.updatePassword(authToken, dtoUpdateInfoManagementRequest.getNewPassword());
        return ResponseEntity.ok("Password berhasil diperbarui.");
    }
}
