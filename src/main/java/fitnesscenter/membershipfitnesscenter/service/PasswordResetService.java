package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.dto.DtoEmailRequest;
import fitnesscenter.membershipfitnesscenter.dto.DtoForgotPasswordRequest;
import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean requestPasswordReset(DtoEmailRequest dtoEmailRequest) {
        Optional<Participant> participantOptional = participantRepository.findByEmail(dtoEmailRequest.getEmail());

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();

            String resetToken = generateResetToken();

            participant.setResetToken(resetToken);

            participantRepository.save(participant);

            sendPasswordResetEmail(participant, resetToken);

            return true;
        }
        return false;
    }

    public boolean confirmPasswordReset(DtoForgotPasswordRequest dtoForgotPasswordRequest) {
        Optional<Participant> participantOptional = participantRepository.findByResetToken(dtoForgotPasswordRequest.getResetToken());

        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            participant.setResetToken(null);
            participant.setPassword(passwordEncoder.encode(dtoForgotPasswordRequest.getNewPassword()));
            participantRepository.save(participant);
            return true;
        }
        return false;
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private void sendPasswordResetEmail(Participant participant, String resetToken) {
        String subject = "Reset Password";
        String text = "Anda telah meminta reset password. Berikut token untuk mereset kata sandi Anda: ".concat(resetToken);

        try {
            emailService.sendEmailPasswordReset(participant.getEmail(), subject, text);
        }  catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
