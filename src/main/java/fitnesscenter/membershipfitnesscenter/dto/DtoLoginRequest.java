package fitnesscenter.membershipfitnesscenter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoLoginRequest {
    private String email;
    private String password;
}
