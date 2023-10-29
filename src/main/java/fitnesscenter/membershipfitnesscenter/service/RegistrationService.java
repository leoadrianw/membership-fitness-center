package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.dto.DtoRegisterRequest;
import fitnesscenter.membershipfitnesscenter.dto.DtoVerificationRequest;
import fitnesscenter.membershipfitnesscenter.model.AuthToken;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IAuthTokenRepository;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import javax.mail.Part;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@Service
public class RegistrationService {
    @Autowired
    private IAuthTokenRepository authTokenRepository;
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(DtoRegisterRequest request, String verificationCode) throws MessagingException {

        if (participantRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email sudah digunakan.");
        }

        Participant participant = new Participant();
        participant.setName(request.getName());
        participant.setEmail(request.getEmail());
        participant.setPassword(passwordEncoder.encode(request.getPassword()));
        participant.setPhoneNumber(request.getPhoneNumber());
        participant.setVerified(false);
        participant.setVerificationCode(verificationCode);

        String creditCardInfo = encryptCreditCardInfo(request.getCreditCard().getCardNo() +
                request.getCreditCard().getCvv() +
                request.getCreditCard().getExpiredDate() +
                request.getCreditCard().getOwnerName());

        participant.setCreditCardInfo(creditCardInfo);

        participantRepository.save(participant);

        return verificationCode;
    }

    public boolean validateOtp(String token, DtoVerificationRequest verificationRequest) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (authToken != null) {
            Participant participant = authToken.getParticipant();
            if (verificationRequest.getOtp().equals(participant.getVerificationCode())) {
                participant.setVerified(true);
                participantRepository.save(participant);
                return true;
            }
        }
        return false;
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
