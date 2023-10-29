package fitnesscenter.membershipfitnesscenter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int durationInMinutes;

    private String description;

    @ManyToOne
    private ServiceMenu serviceMenu;

}
