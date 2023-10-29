package fitnesscenter.membershipfitnesscenter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int remainingSessions;

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private ServiceMenu serviceMenu;

}
