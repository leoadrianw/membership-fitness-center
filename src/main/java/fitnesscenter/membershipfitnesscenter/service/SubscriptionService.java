package fitnesscenter.membershipfitnesscenter.service;


import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.model.Subscription;
import fitnesscenter.membershipfitnesscenter.repository.IAuthTokenRepository;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import fitnesscenter.membershipfitnesscenter.repository.IServiceMenuRepository;
import fitnesscenter.membershipfitnesscenter.repository.ISubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private IAuthTokenRepository authTokenRepository;

    @Autowired
    private IParticipantRepository participantRepository;

    @Autowired
    private IServiceMenuRepository serviceMenuRepository;

    public List<Subscription> getSubscriptionList(String token) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Participant participant = authToken.getParticipant();
            return subscriptionRepository.findAllByParticipant(participant);
        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }
    }

    public Subscription subscribeToService(String token, Long serviceMenuId) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Participant participant = authToken.getParticipant();
            ServiceMenu serviceMenu = serviceMenuRepository.findById(serviceMenuId)
                    .orElseThrow(() -> new EntityNotFoundException("ServcieMenu not found with ID: " + serviceMenuId));

            Optional<Subscription> existingSubscription = subscriptionRepository.findByParticipantAndServiceMenu(participant, serviceMenu);

            Subscription subscription;
            if (existingSubscription.isPresent()) {
                subscription = existingSubscription.get();
            } else {
                subscription = new Subscription();
            }
            subscription.setParticipant(participant);
            subscription.setServiceMenu(serviceMenu);
            subscription.setStartDate(LocalDateTime.now());
            subscription.setEndDate(LocalDateTime.now().plusDays(30));
            subscription.setRemainingSessions(serviceMenu.getTotalSessions());

            participant.setBillAmount(BigDecimal.valueOf(serviceMenu.getPricePerSession() * serviceMenu.getTotalSessions()));
            participantRepository.save(participant);
            return subscriptionRepository.save(subscription);
        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }

    }

    public void cancelSubscription(String token, Long subscriptionId) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(subscriptionId);
            if (subscriptionOptional.isPresent()) {
                subscriptionRepository.delete(subscriptionOptional.get());
            } else {
                throw new EntityNotFoundException("Kode langganan tidak ditemukan");
            }

        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }
    }

    public void extendSubscription(String token, Long subscriptionId, int numberOfSessions) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(subscriptionId);
            if (subscriptionOptional.isPresent()) {
                Subscription subscription = subscriptionOptional.get();
                subscription.setEndDate(subscription.getEndDate().plusDays(numberOfSessions));
                subscription.setRemainingSessions(subscription.getRemainingSessions() + numberOfSessions);
                subscriptionRepository.save(subscription);
            } else {
                throw new EntityNotFoundException("Kode langganan tidak ditemukan");
            }
        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }
    }
}
