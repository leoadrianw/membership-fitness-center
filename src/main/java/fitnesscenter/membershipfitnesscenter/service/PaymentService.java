package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PaymentService {
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private EmailService emailService;

    public boolean verifyPaymentOTP(String email, String otp) {
        Optional<Participant> participantOptional = participantRepository.findByEmail(email);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();

            if (participant.getPaymentOTP() != null &&
                    participant.getPaymentOTP().equals(otp) &&
                    LocalDateTime.now().isBefore(participant.getPaymentOTPExpiration())) {

                participant.setPaymentStatus(true);
                participant.setPaymentOTP(null);
                participant.setPaymentOTPExpiration(null);


                participantRepository.save(participant);
                return true;
            }
        }

        return false;
    }

    public boolean verifyBillAmount(Long participantId, BigDecimal expectedBillAmount) {
        Optional<Participant> participantOptional = participantRepository.findById(participantId);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();

            if (participant.getBillAmount() != null && participant.getBillAmount().compareTo(expectedBillAmount) == 0) {
                participant.setBillVerified(true);
                participantRepository.save(participant);
                return true;
            }
        }

        return false;
    }


    public boolean isPaymentOTPExpired(String email) {
        Optional<Participant> participantOptional = participantRepository.findByEmail(email);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            if (participant.getPaymentOTPExpiration() != null &&
                    LocalDateTime.now().isAfter(participant.getPaymentOTPExpiration())) {
                return true;
            }
        }

        return false;
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

    public void sendEmailVerification(String email, Long participantId) throws MessagingException {
        String code = generateVerificationCode();

        Optional<Participant> participantOptional = participantRepository.findById(participantId);

        if (participantOptional.isPresent()) {
           Participant participant = participantOptional.get();
           participant.setPaymentOTP(code);
           participantRepository.save(participant);
        }

        String subject = "Kode OTP";
        String text = "Kode OTP anda adalah : " + code;
        emailService.sendEmail(email,subject,text);
    }
}
