package fitnesscenter.membershipfitnesscenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoAddServiceMenuRequest {
    private String name;
    @JsonProperty("price_per_session")
    private double pricePerSession;
    @JsonProperty("total_sessions")
    private int totalSessions;
    private String schedule;
    @JsonProperty("duration_in_minutes")
    private int durationInMinutes;
    @JsonProperty("exercise_list")
    private List<DtoExercise> exerciseList;
}
