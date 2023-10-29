package fitnesscenter.membershipfitnesscenter.repository;

import fitnesscenter.membershipfitnesscenter.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByEmail(String email);

    Optional<Participant> findParticipantByVerificationCode(String verificationCode);
}
