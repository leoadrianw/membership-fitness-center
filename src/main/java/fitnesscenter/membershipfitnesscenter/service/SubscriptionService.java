package fitnesscenter.membershipfitnesscenter.service;


import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.model.Subscription;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import fitnesscenter.membershipfitnesscenter.repository.IServiceMenuRepository;
import fitnesscenter.membershipfitnesscenter.repository.ISubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private IParticipantRepository participantRepository;

    @Autowired
    private IServiceMenuRepository serviceMenuRepository;

    public Subscription subscribeToService(Long participantId, Long serviceMenuId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found with ID: " + participantId));
        ServiceMenu serviceMenu = serviceMenuRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("ServcieMenu not found with ID: " + serviceMenuId));

        Optional<Subscription> existingSubscription = subscriptionRepository.findByParticipantAndServiceMenu(participant, serviceMenu);

        if (existingSubscription.isPresent()) {
            return existingSubscription.get();
        }

        Subscription subscription = new Subscription();
        subscription.setParticipant(participant);
        subscription.setServiceMenu(serviceMenu);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusDays(30));
        subscription.setRemainingSessions(serviceMenu.getTotalSessions());

        participant.setBillAmount(BigDecimal.valueOf(serviceMenu.getPricePerSession()*serviceMenu.getTotalSessions()));
        participantRepository.save(participant);

        return subscriptionRepository.save(subscription);
    }

    public void cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).get();
        subscriptionRepository.delete(subscription);
    }

    public void extendSubscription(Long subscriptionId, int numberOfSessions) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).get();
        subscription.setEndDate(subscription.getEndDate().plusDays(numberOfSessions));
        subscription.setRemainingSessions(subscription.getRemainingSessions() + numberOfSessions);
        subscriptionRepository.save(subscription);
    }
}
