package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IAuthTokenRepository;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private IAuthTokenRepository authTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthToken login(String email, String password) {
        Optional<Participant> participantOptional = participantRepository.findByEmail(email);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            if (passwordEncoder.matches(password, participant.getPassword())) {
                AuthToken authToken = generateAuthToken(participant);
                authTokenRepository.save(authToken);
                return authToken;
            }
        }

        return null;
    }

    public void logout(String token) {
        authTokenRepository.deleteByToken(token);
    }

    public AuthToken refreshToken(String token) {
        AuthToken existingToken = authTokenRepository.findByToken(token);

        if (existingToken != null) {
            existingToken.updateExpiration();
            authTokenRepository.save(existingToken);
            return existingToken;
        }
        return null;
    }

    private AuthToken generateAuthToken(Participant participant) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDateTime = LocalDateTime.now().plusHours(1);
        return new AuthToken(token, expirationDateTime, participant);
    }

}
