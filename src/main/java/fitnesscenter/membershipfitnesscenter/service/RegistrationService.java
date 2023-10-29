package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.model.Participant;
import fitnesscenter.membershipfitnesscenter.repository.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;

@Service
public class RegistrationService {
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private EmailService emailService;

    public Participant register(Participant participant) throws MessagingException {

        if (participantRepository.findByEmail(participant.getEmail()).isPresent()) {
            throw new RuntimeException("Email sudah digunakan.");
        }

        participant.setVerified(false);

        String verificationCode = generateVerificationCode();
        participant.setVerificationCode(verificationCode);

        Participant registeredParticipant = participantRepository.save(participant);

        sendVerificationEmail(registeredParticipant);

        return registeredParticipant;
    }

    private String generateVerificationCode() {
        int codeLength = 6; // Panjang kode verifikasi
        String allowedChars = "0123456789"; // Karakter yang diperbolehkan

        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            verificationCode.append(randomChar);
        }

        return verificationCode.toString();
    }

    private void sendVerificationEmail(Participant participant) throws MessagingException {
        String subject = "Konfirmasi Pendaftaran";
        String text = "Terima kasih atas pendaftaran Anda di pusat kebugaran kami. Silakan masukkan kode verifikasi berikut: " + participant.getVerificationCode();

        try {
            emailService.sendEmail(participant.getEmail(), subject, text);
        } catch (MessagingException e) {
        }
    }
}
