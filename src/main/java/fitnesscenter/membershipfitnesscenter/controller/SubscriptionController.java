package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.DtoSubscriptionRequest;
import fitnesscenter.membershipfitnesscenter.model.Subscription;
import fitnesscenter.membershipfitnesscenter.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/subscription-list")
    public ResponseEntity<List<Subscription>> getSubscriptionList(@RequestHeader("Authorization") String authToken) {
        List<Subscription> subscriptionList = subscriptionService.getSubscriptionList(authToken);
        return ResponseEntity.ok(subscriptionList);
    }
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToService(@RequestHeader("Authorization") String authToken, @RequestBody DtoSubscriptionRequest dtoSubscriptionRequest) {
        subscriptionService.subscribeToService(authToken, dtoSubscriptionRequest.getServiceMenuId());
        return ResponseEntity.ok("Berlangganan berhasil.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSubscription(@RequestHeader("Authorization") String authToken, @RequestBody DtoSubscriptionRequest dtoSubscriptionRequest) {
        subscriptionService.cancelSubscription(authToken, dtoSubscriptionRequest.getSubscriptionId());
        return ResponseEntity.ok("Berhasil membatalkan langganan.");
    }

    @PostMapping("/extend")
    public ResponseEntity<String> extendSubscription(@RequestHeader("Authorization") String authToken, @RequestBody DtoSubscriptionRequest dtoSubscriptionRequest) {
        subscriptionService.extendSubscription(authToken, dtoSubscriptionRequest.getSubscriptionId(), dtoSubscriptionRequest.getNumberOfSessions());
        return ResponseEntity.ok("Durasi pertemuan berhasil ditambahkan.");
    }



}