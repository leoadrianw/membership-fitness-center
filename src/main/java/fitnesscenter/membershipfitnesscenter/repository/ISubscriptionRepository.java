package fitnesscenter.membershipfitnesscenter.repository;

import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByParticipantAndServiceMenu(Participant participant, ServiceMenu serviceMenu);
}
