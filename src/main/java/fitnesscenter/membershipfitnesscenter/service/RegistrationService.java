package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.dto.DtoRegisterRequest;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import java.util.Base64;
import java.util.Random;

@Service
public class RegistrationService {
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(DtoRegisterRequest request) throws MessagingException {

        if (participantRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email sudah digunakan.");
        }

        Participant participant = new Participant();
        participant.setName(request.getName());
        participant.setEmail(request.getEmail());
        participant.setPassword(passwordEncoder.encode(request.getPassword()));
        participant.setPhoneNumber(request.getPhoneNumber());
        participant.setVerified(false);

        String verificationCode = generateVerificationCode();
        participant.setVerificationCode(verificationCode);

        String creditCardInfo = encryptCreditCardInfo(request.getCreditCard().getCardNo() +
                request.getCreditCard().getCvv() +
                request.getCreditCard().getExpiredDate() +
                request.getCreditCard().getOwnerName());

        participant.setCreditCardInfo(creditCardInfo);

        participantRepository.save(participant);
    }

    private String generateVerificationCode() {
        int codeLength = 6;
        String allowedChars = "0123456789";

        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            verificationCode.append(randomChar);
        }

        return verificationCode.toString();
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
