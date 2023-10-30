package fitnesscenter.membershipfitnesscenter.repository;

import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IAuthTokenRepository extends JpaRepository<AuthToken, Long> {
    AuthToken findByToken(String token);

    @Transactional
    void deleteByToken(String token);

    AuthToken findByParticipant(Participant participant);
}
