package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private EmailService emailService;

    public void requestPasswordReset(String email) {
        Optional<Participant> participantOptional = participantRepository.findByEmail(email);

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();

            String resetToken = generateResetToken();

            participant.setResetToken(resetToken);

            participantRepository.save(participant);

            sendPasswordResetEmail(participant, resetToken);
        }
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private void sendPasswordResetEmail(Participant participant, String resetToken) {
        String subject = "Reset Password";
        String text = "Anda telah meminta reset password. Silakan klik link berikut untuk mereset kata sandi Anda: [LINK RESET]";
        String resetLink = "https://fitness.com/reset-password?token=" + resetToken;
        text = text.replace("[LINK RESET]", resetLink);

        try {
            emailService.sendEmail(participant.getEmail(), subject, text);
        }  catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
