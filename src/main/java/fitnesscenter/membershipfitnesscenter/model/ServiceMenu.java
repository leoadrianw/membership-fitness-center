package fitnesscenter.membershipfitnesscenter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class ServiceMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double pricePerSession;

    private int totalSessions;

    private String schedule;

    private int durationInMinutes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMenu")
    private List<Exercise> exercises;

}
