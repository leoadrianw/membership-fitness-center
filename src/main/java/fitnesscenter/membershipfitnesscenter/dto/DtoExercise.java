package fitnesscenter.membershipfitnesscenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoExercise {
    private String name;
    @JsonProperty("duration_in_minutes")
    private int durationInMinutes;
    private String description;
}
