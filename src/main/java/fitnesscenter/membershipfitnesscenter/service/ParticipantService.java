package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    @Autowired
    IParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateFullName(Long participantId, String newFullName) {
        Optional<Participant> participantOptional = participantRepository.findById(participantId);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            participant.setName(newFullName);
            participantRepository.save(participant);
        }
    }

    public void updateCreditCardInfo(Long participantId, String newCreditCardInfo) {
        Optional<Participant> participantOptional = participantRepository.findById(participantId);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            String encryptedCreditCardInfo = encryptCreditCardInfo(newCreditCardInfo);
            participant.setCreditCardInfo(encryptedCreditCardInfo);
            participantRepository.save(participant);
        }
    }

    public void updatePassword(Long participantId, String newPassword) {
        Optional<Participant> participantOptional = participantRepository.findById(participantId);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            String encryptedPassword = passwordEncoder.encode(newPassword);
            participant.setPassword(encryptedPassword);
            participantRepository.save(participant);
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
