package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.dto.DtoCreditCard;
import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IAuthTokenRepository;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Optional;

@Service
public class ParticipantService {
    @Autowired
    private IAuthTokenRepository authTokenRepository;
    @Autowired
    private IParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Participant> findByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    public void updateFullName(String token, String newFullName) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Participant participant = authToken.getParticipant();
            participant.setName(newFullName);
            participantRepository.save(participant);
        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }
    }

    public void updateCreditCardInfo(String token, DtoCreditCard creditCard) {

        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Participant participant = authToken.getParticipant();
            String creditCardInfo = encryptCreditCardInfo(creditCard.getCardNo() +
                    creditCard.getCvv() +
                    creditCard.getExpiredDate() +
                    creditCard.getOwnerName());
            String encryptedCreditCardInfo = encryptCreditCardInfo(creditCardInfo);
            participant.setCreditCardInfo(encryptedCreditCardInfo);
            participantRepository.save(participant);
        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }
    }

    public void updatePassword(String token, String newPassword) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Participant participant = authToken.getParticipant();
            String encryptedPassword = passwordEncoder.encode(newPassword);
            participant.setPassword(encryptedPassword);
            participantRepository.save(participant);
        } else {
            throw new EntityNotFoundException("Peserta tidak ditemukan.");
        }
    }

    private String encryptCreditCardInfo(String creditCardInfo) {
        try {
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedData = cipher.doFinal(creditCardInfo.getBytes());

            String encryptedCreditCardInfo = Base64.getEncoder().encodeToString(encryptedData);

            return encryptedCreditCardInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
