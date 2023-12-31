package fitnesscenter.membershipfitnesscenter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ServiceMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonProperty("price_per_session")
    private double pricePerSession;

    @JsonProperty("total_sessions")
    private int totalSessions;

    private String schedule;

    @JsonProperty("duration_in_minutes")
    private int durationInMinutes;

    @JsonProperty("exercise_list")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMenu")
    private List<Exercise> exerciseList;

}
