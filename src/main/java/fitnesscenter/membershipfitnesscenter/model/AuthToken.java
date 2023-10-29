package fitnesscenter.membershipfitnesscenter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="token")
    private String token;

    private LocalDateTime expirationDateTime;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    public AuthToken(String token, LocalDateTime expirationDateTime, Participant participant) {
    }

    public AuthToken() {

    }

    public void updateExpiration() {
        this.expirationDateTime = LocalDateTime.now().plusHours(1);
    }

}
