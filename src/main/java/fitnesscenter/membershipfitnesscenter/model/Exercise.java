package fitnesscenter.membershipfitnesscenter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
